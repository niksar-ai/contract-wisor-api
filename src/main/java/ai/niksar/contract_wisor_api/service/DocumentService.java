package ai.niksar.contract_wisor_api.service;

import io.temporal.client.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.config.TemporalConfig;
import ai.niksar.contract_wisor_api.dto.*;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.*;
import ai.niksar.contract_wisor_api.repository.DocumentContentRepository;
import ai.niksar.contract_wisor_api.repository.DocumentDetailRepository;
import ai.niksar.contract_wisor_api.repository.DocumentRepository;
import ai.niksar.contract_wisor_api.scheduler.WorkflowMonitoringScheduler;
import ai.niksar.contract_wisor_api.specification.DocumentSpecification;
import ai.niksar.contract_wisor_api.util.Constants;
import ai.niksar.contract_wisor_api.util.Util;
import ai.niksar.contract_wisor_api.workflow.DocumentAnalysisWorkflow;
import ai.niksar.contract_wisor_api.workflow.DocumentParseWorkflow;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static ai.niksar.contract_wisor_api.util.Util.parseStringToBoolean;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private DocumentContentRepository documentContentRepository;
    @Autowired
    private DocumentDetailRepository documentDetailRepository;
    @Autowired
    @Lazy
    private DocumentTypeService documentTypeService;
    @Autowired
    private DocumentRelationService documentRelationService;
    @Autowired
    private DocumentMetadataService documentMetadataService;
    @Autowired
    private DocumentViewService documentViewService;
    @Autowired
    private DocumentCommentService documentCommentService;
    @Autowired
    private DocumentFavouriteService documentFavService;
    @Autowired
    private WorkflowClient workflowClient;
    @Autowired
    private AsyncDocumentProcessor asyncDocumentProcessor;
    @Autowired
    private TemporalConfig temporalConfig;
    @Autowired
    @Lazy
    private WorkflowMonitoringScheduler workflowMonitoringScheduler;
    @Autowired
    @Lazy
    private AnalyzeService analyzeService;
    @Autowired
    private RoleApiUrlService roleApiUrlService;

    @Value("${temporal.taskQueue}")
    private String taskQueue;
    private static final Logger logger = LoggerFactory.getLogger(DocumentService.class);

    public DocumentListDTO getDocumentList(Integer pageNumber, Integer pageSize, String status, String state, String name, String firstUpdateDate, String lastUpdateDate, String firstCreateDate, String lastCreateDate, Long sizeMin, Long sizeMax, String isMetadataExist, String createUser, String updateUser, UUID documentTypeId, String expiredContract, String description, String company, String expiryDateFirst, String expiryDateLast) {
        DocumentFilterDTO               filterDTO                   = documentFilterDTO(status, state, name, firstUpdateDate, lastUpdateDate, firstCreateDate, lastCreateDate, sizeMin, sizeMax, isMetadataExist, createUser, updateUser, documentTypeId);
        Sort                            sort                        = Sort.by(Sort.Order.desc("updateDate"), Sort.Order.desc("updateTime"));
        Pageable                        pageable                    = PageRequest.of(pageNumber - 1, pageSize > 100 ? 100 : pageSize, sort);
        DocumentMetadataFilterDTO       documentMetadataFilterDTO   = documentMetadataFilterDTO(expiredContract, description, company, expiryDateFirst, expiryDateLast);
        Specification<Document>         spec                        = new DocumentSpecification(filterDTO, documentMetadataFilterDTO);
        Page<Document>                  documentPage                = documentRepository.findAll(spec, pageable);
        DocumentListDTO                 documentListDTO             = new DocumentListDTO();

        documentListDTO.setRecords(documentPage.getContent());
        documentListDTO.setTotalRecords((int) documentPage.getTotalElements());
        documentListDTO.setCurrent(pageNumber);
        documentListDTO.setNumPages(documentPage.getTotalPages());
        documentListDTO.setPerPage(documentPage.getContent().size());

        return documentListDTO;
    }

    public DocumentStatisticsDTO getDocumentStatistics() {
        DocumentStatisticsDTO   documentStatisticsDTO                   = new DocumentStatisticsDTO();
        long                    documentCount                           = documentRepository.count();
        double                  metadataExistCount                      = documentRepository.findDocumentsWithMetadataIdNotNull();
        double                  metadataRate                            = ((metadataExistCount) / documentCount) * 100;
        double                  scale                                   = Math.pow(10, 2);
        double                  roundedValue                            = Math.round(metadataRate * scale) / scale;

        documentStatisticsDTO.setTotalDocumentCount(documentCount);
        documentStatisticsDTO.setMetadataRate(roundedValue);
        documentStatisticsDTO.setStateStatistic(calculatedStatisticByState(documentCount,scale));
        documentStatisticsDTO.setTotalDocumentSize(documentRepository.sumSizeOfDocuments());

        return documentStatisticsDTO;
    }

    private List<Map<String, Object>> calculatedStatisticByState(long documentCount, double scale)  {
        List<Object[]>                  results             = documentRepository.getCountGroupByWithState();
        List<Map<String, Object>>       stateStatisticList  = new ArrayList<>();
        for(Object[] resultItem : results){
            String  stateValue                  = (String) resultItem[0];
            long    documentCountByState        = (long)   resultItem[1];
            float   documentCountByStateRate    = ((float) (documentCountByState) / documentCount) * 100;
            double  roundedValueByState         = Math.round(documentCountByStateRate * scale) / scale;

            Map<String,Object> map = new HashMap<>();
            map.put("state", stateValue);
            map.put("count", documentCountByState);
            map.put("rate", roundedValueByState);

            stateStatisticList.add(map);
        }

        return stateStatisticList;
    }

    public DocumentListDTO getSortedDocumentList(Integer pageNumber, Integer pageSize) {
        Sort                sort                = Sort.by(Sort.Order.desc("updateDate"), Sort.Order.desc("updateTime"));
        Pageable            pageable            = PageRequest.of(pageNumber - 1, pageSize > 100 ? 100 : pageSize, sort);
        Page<Document>      documentPage        = documentRepository.findAll(pageable);
        DocumentListDTO     documentListDTO     = new DocumentListDTO();

        documentListDTO.setRecords(documentPage.getContent());
        documentListDTO.setTotalRecords((int) documentPage.getTotalElements());
        documentListDTO.setCurrent(pageNumber);
        documentListDTO.setNumPages(documentPage.getTotalPages());
        documentListDTO.setPerPage(documentPage.getContent().size());

        return documentListDTO;
    }

    public List<NodeDTO> getDocumentTree(String status, String state, String name, String firstUpdateDate, String lastUpdateDate, String firstCreateDate, String lastCreateDate, Long sizeMin, Long sizeMax, String isMetadataExist, String createUser, String updateUser, UUID documentTypeId, String expiredContract, String description, String company, String expiryDateFirst, String expiryDateLast) {
        DocumentMetadataFilterDTO       documentMetadataFilterDTO   = documentMetadataFilterDTO(expiredContract, description, company, expiryDateFirst, expiryDateLast);
        DocumentFilterDTO               filterDTO                   = documentFilterDTO(status, state, name, firstUpdateDate, lastUpdateDate, firstCreateDate, lastCreateDate, sizeMin, sizeMax, isMetadataExist, createUser, updateUser, documentTypeId);
        Specification<Document>         spec                        = new DocumentSpecification(filterDTO, documentMetadataFilterDTO);
        List<Document>                  documentList                = documentRepository.findAll(spec);
        List<DocumentType>              documentTypeList            = documentTypeService.getDocumentTypeList().stream().filter(item->item.getParentId() == null || getDocumentTypeList(documentList,documentTypeId).contains(item.getId())).collect(Collectors.toList());
        List<DocumentRelation>          documentRelationList        = documentRelationService.getDocumentRelationList(filterDTO, documentMetadataFilterDTO);
        NodeDTO                         root                        = new NodeDTO();

        root.setNodeType("01");

        return buildTree(root, documentTypeList, documentRelationList, documentList);
    }

    private List<UUID> getDocumentTypeList(List<Document> documentList,UUID documentTypeId) {
        List<UUID> idList = new ArrayList<>();

        if(documentTypeId != null){
            idList.add(documentTypeId);
        }
        else{
            documentList.forEach(item -> {
                if (!idList.contains(item.getDocumentTypeId())) {
                    idList.add(item.getDocumentTypeId());
                }
            });
        }

        return idList;
    }

    private List<NodeDTO> buildTree(NodeDTO parent, List<DocumentType> documentTypeList, List<DocumentRelation> documentRelationList, List<Document> documentList) {
        List<NodeDTO> tree = new ArrayList<>();
        if (parent.getNodeType().equals("01")) {
            documentTypeList.stream().filter(e -> Objects.equals(e.getParentId(), parent.getId())).forEach(item -> {
                NodeDTO node = new NodeDTO();
                node.setId(item.getId());
                node.setParentId(item.getParentId());
                node.setName(item.getName());
                node.setData(item);
                node.setNodeType("01");
                node.setChildren(buildTree(node, documentTypeList, documentRelationList, documentList));
                tree.add(node);
            });
            List<Document> documentListByDocumentType = documentList.stream().filter(e -> Objects.equals(e.getDocumentTypeId(), parent.getId())).toList();
            documentListByDocumentType.stream().filter(document -> documentRelationList.stream()
                    .noneMatch(relation ->
                            Objects.equals(relation.getChild().getId(), document.getId())
                                    && documentListByDocumentType.stream().anyMatch(e ->
                                    Objects.equals(relation.getParentId(), e.getId())
                            )
                    )
            ).forEach(item -> {
                NodeDTO node = new NodeDTO();
                node.setId(item.getId());
                node.setParentId(item.getDocumentTypeId());
                node.setName(item.getName());
                node.setNodeType("02");
                node.setData(item);
                node.setChildren(buildTree(node, documentTypeList, documentRelationList, documentList));
                tree.add(node);
            });
        } else if (parent.getNodeType().equals("02")) {
            documentRelationList.stream().filter(e -> Objects.equals(e.getParentId(), parent.getId())).forEach(item -> {
                NodeDTO node = new NodeDTO();
                node.setId(item.getChild().getId());
                node.setParentId(item.getParentId());
                node.setName(item.getChild().getName());
                node.setData(item.getChild());
                node.setNodeType("02");
                node.setChildren(buildTree(node, documentTypeList, documentRelationList, documentList));
                tree.add(node);
            });
        }

        return tree;
    }

    public DocumentDetail getDocumentById(UUID id) {
        DocumentDetail documentDetail = documentDetailRepository.findById(id).orElseThrow(ContractWisorException.E001::new);
        if(documentDetail.getDocumentMetadata() != null){
            DocumentMetadataDTO documentMetadataDTO= new DocumentMetadataDTO();
            documentMetadataDTO.setDocumentId(documentDetail.getDocumentMetadata().getDocumentId());
            documentMetadataDTO.setCreateDate(documentDetail.getDocumentMetadata().getCreateDate());
            documentMetadataDTO.setCreateTime(documentDetail.getDocumentMetadata().getCreateTime());
            documentMetadataDTO.setId(documentDetail.getDocumentMetadata().getId());
            documentMetadataDTO.setExpiryDate(documentDetail.getDocumentMetadata().getExpiryDate());
            documentMetadataDTO.setStatus(documentDetail.getDocumentMetadata().getStatus());
            documentMetadataDTO.setCompany(documentDetail.getDocumentMetadata().getCompany());
            documentMetadataDTO.setDescription(documentDetail.getDocumentMetadata().getDescription());
            if(documentDetail.getDocumentMetadata().getAmended() != null ){
                documentMetadataDTO.setAmended(parseStringToBoolean(documentDetail.getDocumentMetadata().getAmended()));
            }
            if(documentDetail.getDocumentMetadata().getExpiredContract() != null){
                documentMetadataDTO.setExpiredContract(parseStringToBoolean(documentDetail.getDocumentMetadata().getExpiredContract()));
            }
            documentDetail.setMetadata(documentMetadataDTO);
        }
            documentDetail.setIsFavorite(documentFavService.isFavDocument(id));
            documentDetail.setPermission(checkPermissions(documentDetail.getState(), documentDetail.getParseState(), documentDetail.getAnalyzeState()));
            documentViewService.saveDocumentView(documentDetail.getId());


        return documentDetail;
    }

    public DocumentPermissionDTO checkPermissions(String state, String parseState, String analyzeState) {
        DocumentPermissionDTO permissions = new DocumentPermissionDTO();
        DocumentPermissionDTO permissionWithRole= permissionWithRole();
        permissions.setDelete(permissionWithRole.isDelete());
        permissions.setDownload(permissionWithRole.isDownload());
        permissions.setComplete(false);
        permissions.setReOpen(false);
        permissions.setRelationDelete(permissionWithRole.isRelationDelete());
        permissions.setRelationSave(permissionWithRole.isRelationSave());
        permissions.setMetadataSave(permissionWithRole.isMetadataSave());
        permissions.setRelationView(permissionWithRole.isRelationView());
        if(state.equals(Constants.DocumentState.COMPLETED)){
            permissions.setAnalyze(false);
            permissions.setParse(false);
            permissions.setReOpen(permissionWithRole.getReOpen());
        }
        else if(parseState.equals(Constants.ParseState.WAITING) || parseState.equals(Constants.ParseState.FAULTY)){
            permissions.setParse(permissionWithRole.isParse());
            permissions.setAnalyze(false);
        } else if(parseState.equals(Constants.ParseState.PROCESS_IN_PROGRESS) || analyzeState.equals(Constants.AnalyzeState.PROCESS_IN_PROGRESS)){
            permissions.setAnalyze(false);
            permissions.setParse(false);
            permissions.setDelete(false);
        } else if(analyzeState.equals(Constants.AnalyzeState.SUCCESSFUL)){
            permissions.setAnalyze(permissionWithRole.isAnalyze());
            permissions.setParse(false);
            permissions.setComplete(permissionWithRole.getComplete());
        } else {
            permissions.setAnalyze(permissionWithRole.isAnalyze());
            permissions.setParse(false);
        }

        return permissions;
    }
    private DocumentPermissionDTO permissionWithRole(){
        DocumentPermissionDTO permission=new DocumentPermissionDTO();
        List<String> actionPermissionList=roleApiUrlService.actionListByRole(SecurityContextHolder.getContext().getAuthentication().getName());
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_ANALYZE)){
            permission.setAnalyze(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_PARSE)){
            permission.setParse(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_COMPLETE)){
            permission.setComplete(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_DELETE)){
            permission.setDelete(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_DOWNLOAD)){
            permission.setDownload(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_REOPEN)){
            permission.setReOpen(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_METADATA_SAVE)){
            permission.setMetadataSave(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_RELATION_SAVE)){
            permission.setRelationSave(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_RELATION_DELETE)){
            permission.setRelationDelete(true);
        }
        if(actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_RELATION_VIEW) || actionPermissionList.contains(Constants.ActionCodes.DOCUMENT_RELATION_VIEW_TREE)){
            permission.setRelationView(true);
        }
        return permission;
    }

    public Document getDocById(UUID id) {
        return documentRepository.findById(id).orElseThrow(ContractWisorException.E006::new);
    }

    public void saveDocument(Document document) {
        documentRepository.save(document);
    }

    public void uploadDocument(DocumentContent document) {
        try {
            document.setParseState(Constants.ParseState.PROCESS_IN_PROGRESS);
            DocumentContent savedDocument = documentContentRepository.save(document);
            asyncDocumentProcessor.processDocumentAsync(savedDocument);
            workflowMonitoringScheduler.checkWorkflowStatusPeriodically(savedDocument.getId(),temporalConfig.workflowServiceStubs());
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while saving the document");
        }
    }

    public ResponseDTO saveDocumentRelation(DocumentChildDTO documentChildDTO, UUID id) {
            Document document               = getDocById(id);
            List<String> relationList           = Arrays.asList(Constants.RelationTypes.ATTACHMENT, Constants.RelationTypes.SUB_FILE);
            ResponseDTO response= new ResponseDTO();
            if(documentChildDTO.getRelationType() == null || !relationList.contains(documentChildDTO.getRelationType()))  throw new ContractWisorException.E019();

            DocumentRelation documentRelation   = new DocumentRelation();
            documentRelation.setRelationType(documentChildDTO.getRelationType());
            documentRelation.setParentId(id);

            Document childDoc                   = getDocById(documentChildDTO.getChildId());
            if(id.equals(childDoc.getId())) throw new ContractWisorException.E025();

            documentRelation.setChild(childDoc);
            document.setUpdateTime(Util.getRealTime());
            document.setUpdateDate(Util.getRealDate());
            String username = SecurityContextHolder.getContext().getAuthentication().getName();
            document.setUpdateUser(username);

            documentRepository.save(document);
            documentRelationService.saveDocumentRelation(documentRelation);

            response.setMessage("Relation saved successfully.");
            return response;
    }

    public DocumentComment saveComment(DocumentComment comment, UUID documentId) {
        return documentCommentService.saveDocumentComment(comment, documentId);
    }

    public List<DocumentComment> listComment(UUID documentId) {
        return documentCommentService.listComment(documentId);
    }

    public ResponseDTO deleteComment(UUID id) {
        return documentCommentService.deleteComment(id);
    }

    public ResponseDTO deleteDocumentRelation(UUID parentId,UUID childId) {
        ResponseDTO response = new ResponseDTO();
        DocumentRelation documentRelation = documentRelationService.getRelationsByChildIdAndParentId(parentId,childId);
        if(documentRelation == null ){
            throw new ContractWisorException.E020();
        }
        Document document  = getDocById(parentId);
        document.setUpdateTime(Util.getRealTime());
        document.setUpdateDate(Util.getRealDate());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        document.setUpdateUser(username);
        documentRepository.save(document);
        documentRelationService.deleteDocumentRelation(documentRelation);
        response.setMessage("Relation deleted successfully");
        return response;
    }

    public ResponseDTO saveMetadata(DocumentMetadata metadata, UUID documentId) {
        DocumentMetadata docMetadata    = documentMetadataService.saveDocumentMetadata(metadata, documentId);
        Document document               = getDocById(documentId);
        ResponseDTO response= new ResponseDTO();
        document.setMetadataId(docMetadata.getId());
        document.setUpdateTime(Util.getRealTime());
        document.setUpdateDate(Util.getRealDate());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        document.setUpdateUser(username);
        documentRepository.save(document);
        response.setMessage("Metadata saved successfully.");
        return response;
    }

    @Transactional
    public ResponseDTO deleteDocument(UUID documentId) {
        Document document = getDocById(documentId);
        ResponseDTO response= new ResponseDTO();
        if (document.getId() != null) {
            documentRepository.delete(document);
            documentRepository.flush();
            documentRelationService.getRelationsByParentId(documentId).forEach(item -> documentRelationService.deleteDocumentRelation(item));
            documentRelationService.getRelationsByChildId(documentId).forEach(item -> documentRelationService.deleteDocumentRelation(item));
            documentMetadataService.deleteByDocumentId(documentId);
            analyzeService.deleteAnalyzeByDocumentId(documentId);
            response.setMessage("Document deleted successfully");
        } else {
            response.setMessage("Document does not exist in the system.");
        }
        return response;
    }

    public DocumentContent getDocumentContent(UUID id) {
        return documentContentRepository.findById(id).orElseThrow(ContractWisorException.E001::new);
    }

    public DocumentContent getDocumentTextContent(UUID id) {
        return documentContentRepository.findById(id).orElseThrow(ContractWisorException.E001::new);
    }

    public ResponseDTO startDocumentParseWorkflow(UUID docId) throws Exception {
        Document document = documentRepository.findById(docId).orElseThrow(ContractWisorException.E006::new);
        String workflowId = document.getId().toString();
        WorkflowOptions options = WorkflowOptions.newBuilder()
                .setTaskQueue(taskQueue)
                .setWorkflowId(workflowId)
                .setWorkflowExecutionTimeout(Duration.ofMinutes(20))
                .build();

        DocumentParseWorkflow workflow = workflowClient.newWorkflowStub(DocumentParseWorkflow.class, options);
        ResponseDTO response= new ResponseDTO();
        try {
            WorkflowClient.start(workflow::processDocument, docId);
            updateDocumentParseState(docId, Constants.ParseState.PROCESS_IN_PROGRESS);
            workflowMonitoringScheduler.checkWorkflowStatusPeriodically(docId,temporalConfig.workflowServiceStubs());
            response.setMessage("Parsing has been started for the document.");
            return response;
        } catch (WorkflowExecutionAlreadyStarted e) {
            throw new ContractWisorException.E021();
        } catch (Exception e) {
            e.printStackTrace();
            updateDocumentParseState(docId, Constants.ParseState.FAULTY);
            throw new ContractWisorException.CustomException("An error occurred while starting parsing for the document: " + e.getMessage());
        }
    }
    public ResponseDTO analyzeDocument(UUID id) throws Exception {
        Document document = documentRepository.findById(id).orElseThrow(ContractWisorException.E006::new);
        ResponseDTO response= new ResponseDTO();
        if (!document.getParseState().equals(Constants.ParseState.SUCCESSFUL)) {
            throw new Exception("Parsing has not been completed successfully for this document. Please run the parsing operation before analyzing.");
        } else {
            String username     = SecurityContextHolder.getContext().getAuthentication().getName();
            String workflowId   = id.toString();

            WorkflowOptions options = WorkflowOptions.newBuilder()
                    .setTaskQueue(taskQueue)
                    .setWorkflowId(workflowId)
                    .setWorkflowExecutionTimeout(Duration.ofMinutes(30))
                    .build();

            DocumentAnalysisWorkflow workflow = workflowClient.newWorkflowStub(DocumentAnalysisWorkflow.class, options);

            try {
                WorkflowClient.start(workflow::analyzeDocument, id, username);
                updateDocumentAnalyzeState(id, Constants.AnalyzeState.PROCESS_IN_PROGRESS);
                workflowMonitoringScheduler.checkWorkflowStatusPeriodically(id,temporalConfig.workflowServiceStubs());
                response.setMessage("Analysis has been started for the document.");
                return response;
            } catch (WorkflowExecutionAlreadyStarted e) {
                throw new ContractWisorException.E021();
            } catch (WorkflowException e) {
                e.printStackTrace();
                updateDocumentAnalyzeState(id, Constants.AnalyzeState.FAULTY);
                throw new ContractWisorException.CustomException("An error occurred while starting analysis for the document: " + e.getMessage());
            }
        }
    }

    public DocumentFilterDTO documentFilterDTO(String status, String state, String name, String firstUpdateDate, String lastUpdateDate, String firstCreateDate, String lastCreateDate, Long sizeMin, Long sizeMax, String isMetadataExist, String createUser, String updateUser, UUID documentTypeId) {
        DocumentFilterDTO filterDTO = new DocumentFilterDTO();

        filterDTO.setStatus(status);
        filterDTO.setState(state);
        filterDTO.setName(name);
        filterDTO.setFirstUpdateDate(firstUpdateDate);
        filterDTO.setLastUpdateDate(lastUpdateDate);
        filterDTO.setFirstCreateDate(firstCreateDate);
        filterDTO.setLastCreateDate(lastCreateDate);
        filterDTO.setSizeMin(sizeMin);
        filterDTO.setSizeMax(sizeMax);
        filterDTO.setIsMetadataExist(isMetadataExist);
        filterDTO.setCreateUser(createUser);
        filterDTO.setUpdateUser(updateUser);
        filterDTO.setDocumentTypeId(documentTypeId);

        return filterDTO;
    }

    public DocumentListDTO getSortedDocumentViewList(Integer pageNumber, Integer pageSize) {
        Pageable                pageable                = PageRequest.of(pageNumber - 1, pageSize > 100 ? 100 : pageSize);
        String                  username                = SecurityContextHolder.getContext().getAuthentication().getName();
        Page<Document>          documentPage            = documentRepository.sortedDocumentsView(username, pageable);
        DocumentListDTO         documentListDTO         = new DocumentListDTO();

        documentListDTO.setRecords(documentPage.getContent());
        documentListDTO.setTotalRecords((int) documentPage.getTotalElements());
        documentListDTO.setCurrent(pageNumber);
        documentListDTO.setNumPages(documentPage.getTotalPages());
        documentListDTO.setPerPage(documentPage.getContent().size());

        return documentListDTO;
    }

    public DocumentRelationListDTO getRelationList(UUID documentId, Integer pageNumber, Integer pageSize) {
        Pageable                    pageable                = PageRequest.of(pageNumber - 1, pageSize > 100 ? 100 : pageSize);
        Page<DocumentRelation>      documentRelations       = documentRelationService.getRelationsByParentIdPageable(documentId, pageable);
        DocumentRelationListDTO     documentListDTO         = new DocumentRelationListDTO();

        documentListDTO.setRecords(documentRelations.getContent());
        documentListDTO.setTotalRecords((int) documentRelations.getTotalElements());
        documentListDTO.setCurrent(pageNumber);
        documentListDTO.setNumPages(documentRelations.getTotalPages());
        documentListDTO.setPerPage(documentRelations.getContent().size());

        return documentListDTO;
    }

    public List<UserModel> getDocumentFavoritedUsers(UUID documentId) {
        return documentFavService.listUsersByDocId(documentId);
    }

    public List<NodeDTO> getDocumentRelationTree(UUID id) {
        List<Document> relatedDocuments = documentRepository.findRelatedDocuments(id);

        return buildRelationTree(id, relatedDocuments);
    }

    private List<NodeDTO> buildRelationTree(UUID parentId, List<Document> relatedDocuments) {
        List<NodeDTO> tree = new ArrayList<>();
        relatedDocuments.forEach(item -> {
            List<Document> relatedDocument = documentRepository.findRelatedDocuments(item.getId());
            NodeDTO node = new NodeDTO();
            node.setId(item.getId());
            node.setParentId(parentId);
            node.setName(item.getName());
            item.setRelationType(documentRelationService.getRelationsByChildIdAndParentId(parentId,item.getId()).getRelationType());
            node.setData(item);
            node.setChildren(buildRelationTree(item.getId(), relatedDocument));
            tree.add(node);
        });

        return tree;
    }

    @Transactional
    public Document updateDocumentState(UUID documentId, String state) {
        Document document = documentRepository.findById(documentId).orElseThrow(ContractWisorException.E006::new);

        document.setState(state);
        document.setUpdateDate(Util.getRealDate());
        document.setUpdateTime(Util.getRealTime());

        return documentRepository.save(document);
    }

    @Transactional
    public Document updateDocumentParseState(UUID documentId, String state) {
        Document document = documentRepository.findById(documentId).orElseThrow(ContractWisorException.E006::new);
        document.setParseState(state);
        document.setUpdateDate(Util.getRealDate());
        document.setUpdateTime(Util.getRealTime());

        return documentRepository.save(document);
    }
    @Transactional
    public void updateActiveDocumentState(UUID docId) {
        Document document = documentRepository.findById(docId).orElse(null);
        if (document != null && document.getParseState().equals(Constants.ParseState.PROCESS_IN_PROGRESS)) {
            document.setParseState(Constants.ParseState.FAULTY);
            document.setUpdateDate(Util.getRealDate());
            document.setUpdateTime(Util.getRealTime());
            documentRepository.save(document);
            logger.info("Document state updated: " + docId);
        }
        else if(document != null && document.getAnalyzeState().equals(Constants.AnalyzeState.PROCESS_IN_PROGRESS)){
            document.setAnalyzeState(Constants.AnalyzeState.FAULTY);
            document.setUpdateDate(Util.getRealDate());
            document.setUpdateTime(Util.getRealTime());
            documentRepository.save(document);
            logger.info("Document state updated: " + docId);
        }
    }
    public List<Document> activeDocumentList(){
        return documentRepository.progressDocumentList();
    }

    @Transactional
    public Document updateDocumentAndParseState(UUID documentId, String state, String parseState) {
        Document document = documentRepository.findById(documentId).orElseThrow(ContractWisorException.E006::new);

        document.setState(state);
        document.setParseState(parseState);
        document.setUpdateDate(Util.getRealDate());
        document.setUpdateTime(Util.getRealTime());

        return documentRepository.save(document);
    }

    @Transactional
    public Document updateDocumentAnalyzeState(UUID documentId, String state) {
        Document document = documentRepository.findById(documentId).orElseThrow(ContractWisorException.E006::new);

        document.setAnalyzeState(state);
        document.setUpdateDate(Util.getRealDate());
        document.setUpdateTime(Util.getRealTime());

        return documentRepository.save(document);
    }

    @Transactional
    public Document updateDocumentStateAndAnalyzeState(UUID documentId, String state, String analyzeState) {
        Document document = documentRepository.findById(documentId).orElseThrow(ContractWisorException.E006::new);

        document.setAnalyzeState(analyzeState);
        document.setState(state);
        document.setUpdateDate(Util.getRealDate());
        document.setUpdateTime(Util.getRealTime());

        return documentRepository.save(document);
    }

    private DocumentMetadataFilterDTO documentMetadataFilterDTO(String expiredContract, String description, String company, String expiryDateFirst, String expiryDateLast) {
        DocumentMetadataFilterDTO filterDTO = new DocumentMetadataFilterDTO();

        filterDTO.setExpiredContract(expiredContract);
        filterDTO.setDescription(description);
        filterDTO.setCompany(company);
        filterDTO.setExpiryDateFirst(expiryDateFirst);
        filterDTO.setExpiryDateLast(expiryDateLast);

        return filterDTO;
    }

    public String updateDocumentStates() {
        DocumentFilterDTO               filterDTO                       = new DocumentFilterDTO();
        DocumentMetadataFilterDTO       documentMetadataFilterDTO       = new DocumentMetadataFilterDTO();

        filterDTO.setParseState(Constants.ParseState.PROCESS_IN_PROGRESS);

        Specification<Document>         spec                            = new DocumentSpecification(filterDTO, documentMetadataFilterDTO);
        List<Document>                  documentList                    = documentRepository.findAll(spec);

        documentList.forEach(item -> {
            item.setParseState(Constants.ParseState.FAULTY);
            documentRepository.save(item);
        });

        filterDTO.setParseState(null);
        filterDTO.setAnalyzeState(Constants.AnalyzeState.PROCESS_IN_PROGRESS);

        List<Document> newDocumentList = documentRepository.findAll(spec);

        newDocumentList.forEach(item -> {
            item.setAnalyzeState(Constants.AnalyzeState.FAULTY);
            documentRepository.save(item);
        });

        return "Document status update operations completed successfully.";
    }

    public Page<Document> documentPageable(Specification<Document> specification,Pageable pageable){
        return documentRepository.findAll(specification,pageable);
    }

    public void reopenDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId).orElseThrow(ContractWisorException.E001::new);

        if(document.getAnalyzeState().equals(Constants.AnalyzeState.SUCCESSFUL)) document.setState(Constants.DocumentState.ANALYZED);
        else if(document.getParseState().equals(Constants.ParseState.SUCCESSFUL)) document.setState(Constants.DocumentState.PARSED);
        else document.setState(Constants.DocumentState.LOADED);

        documentRepository.save(document);
    }
}
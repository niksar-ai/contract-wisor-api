package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.DocumentTypeCountDTO;
import ai.niksar.contract_wisor_api.dto.DocumentTypeSizeDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.DocumentType;
import ai.niksar.contract_wisor_api.repository.DocumentTypeRepository;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DocumentTypeService {

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    @Lazy
    private DocumentService documentService;

    public List<DocumentType> getDocumentTypeList() {
        return documentTypeRepository.findAll();
    }

    public DocumentType saveDocumentType(DocumentType documentType) {
        return documentTypeRepository.save(documentType);
    }

    public void deleteDocumentType(UUID id) {
        documentTypeRepository.deleteById(id);
    }

    public DocumentType updateDocumentType(UUID id, String name, UUID parentId) {
        DocumentType documentType = documentTypeRepository.findById(id).orElseThrow(ContractWisorException.E023::new);
        documentType.setName(name);
        return documentTypeRepository.save(documentType);
    }

    public List<DocumentType> getDocumentTypeTree() {
        return documentTypeRepository.findByParentId(null).stream().map(this::buildTree).collect(Collectors.toList());
    }

    private DocumentType buildTree(DocumentType node) {
        List<DocumentType> children = documentTypeRepository.findByParentId(node.getId());
        node.setChildren(children);
        children.forEach(this::buildTree);
        return node;
    }

    public List<DocumentTypeCountDTO> typeListWithDocCount(Integer size) {
        List<UUID> idList = new ArrayList<>();
        List<DocumentTypeCountDTO> list = documentTypeRepository.findDocumentTypesByDocCount(PageRequest.of(0, size));
        list.forEach(item -> idList.add(item.getId()));
        Long otherDocumentsOfCount = otherDocumentsOfCount(idList);
        if (otherDocumentsOfCount != 0) {
            DocumentTypeCountDTO otherDocCount = new DocumentTypeCountDTO(UUID.randomUUID(), "Other", otherDocumentsOfCount.intValue());
            list.add(otherDocCount);
        }
        return list;
    }

    public List<DocumentTypeSizeDTO> getDocumentTypeWithTotalSize(Integer size) {
        List<UUID> idList = new ArrayList<>();
        List<DocumentTypeSizeDTO> list = documentTypeRepository.findDocumentTypeWithTotalSize(PageRequest.of(0, size));
        list.forEach(item -> idList.add(item.getId()));
        BigInteger otherDocumentsOfSize = otherDocumentsOfSize(idList);
        if (otherDocumentsOfSize != null) {
            DocumentTypeSizeDTO otherDocSize = new DocumentTypeSizeDTO(UUID.randomUUID(), "Other", otherDocumentsOfSize.intValue());
            list.add(otherDocSize);
        }
        return list;
    }

    public BigInteger otherDocumentsOfSize(List<UUID> excludedIds) {
        return documentTypeRepository.otherDocumentsOfSize(excludedIds);
    }

    public Long otherDocumentsOfCount(List<UUID> excludedIds) {
        return documentTypeRepository.otherDocumentsOfCount(excludedIds);
    }
    public List<DocumentType> documentTypeList(){
        return documentTypeRepository.findAll();
    }
}

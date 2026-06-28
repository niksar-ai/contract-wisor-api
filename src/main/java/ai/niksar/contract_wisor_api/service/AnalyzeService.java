package ai.niksar.contract_wisor_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.dto.*;
import ai.niksar.contract_wisor_api.dto.AnalyzeDTO;
import ai.niksar.contract_wisor_api.exception.ContractWisorException;
import ai.niksar.contract_wisor_api.model.*;
import ai.niksar.contract_wisor_api.repository.*;
import ai.niksar.contract_wisor_api.specification.AnalyzeSpecification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ai.niksar.contract_wisor_api.util.Constants.GeneralKeys;

@Service
public class AnalyzeService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private AnalyzeRepository analyzeRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AnalyzeQuestionRepository analyzeQuestionRepository;
    @Autowired
    private AnalyzeDetailRepository analyzeDetailRepository;
    @Autowired
    private AnalyzeAnswerRepository analyzeAnswerRepository;
    @Autowired
    private DocumentService documentService;
    @Autowired
    private AcceptedAnswerRepository acceptedAnswerRepository;
    @Autowired
    private AnalyzeStatsRepository analyzeStatsRepository;

    @Transactional
    public AnalyzeListDTO getAnalysis(String version,int pageNumber,int pageSize,String state,String status,LocalDateTime createdAtStart,LocalDateTime createdAtEnd,LocalDateTime updatedAtStart,LocalDateTime updatedAtEnd,LocalDateTime processStartedStart,LocalDateTime processStartedEnd,LocalDateTime processFinishedStart,LocalDateTime processFinishedEnd,String createUser,String updateUser,UUID documentId,String documentName,UUID documentTypeId,String isMetadataExist,String expiredContract,String company,String expiryDateFirst,String expiryDateLast) {
        AnalyzeListDTO              analyzeListDTO                  = new AnalyzeListDTO();
        AnalyzeFilterDTO            filterDTO                       = analyzeFilterDTO( state, status, createdAtStart, createdAtEnd, updatedAtStart, updatedAtEnd, processStartedStart, processStartedEnd, processFinishedStart, processFinishedEnd, createUser,updateUser,documentId,documentName,documentTypeId,isMetadataExist);
        DocumentMetadataFilterDTO   documentMetadataFilterDTO       = documentMetadataFilterDTO(expiredContract, company, expiryDateFirst, expiryDateLast);
        AnalyzeSpecification        analyzeSpecification            = new AnalyzeSpecification(filterDTO,documentMetadataFilterDTO,false);
        Sort                        sort                            = Sort.by(Sort.Order.desc(GeneralKeys.PROCESS_STARTED));
        Pageable                    pageable                        = PageRequest.of(pageNumber -1, Math.min(pageSize, 100), sort);
        Page<Analyze>               analyzeList                     = analyzeRepository.findAll(analyzeSpecification,pageable);
        List<Map<String, Object>>   recordsList                     = new ArrayList<>();

        analyzeList.forEach(item->{
            Map<String, Object> records = new HashMap<>();
            AnalyzeStats analyzeStats   = analyzeStatsRepository.findByAnalyzeId(item.getId(),version);

            records.put("document",                                     item.getDocument());
            records.put("id",                                           item.getId());
            records.put(GeneralKeys.PROCESS_STARTED,                item.getProcessStarted().toString());
            records.put("processFinished",                              item.getProcessFinished().toString());
            records.put("totalTokens",                                  analyzeStats != null    &&  analyzeStats.getTotalTokens() != null           ? analyzeStats.getTotalTokens(): 0);
            records.put("promptTokens",                                 analyzeStats != null    &&  analyzeStats.getPromptTokens() != null          ? analyzeStats.getPromptTokens() :0);
            records.put("completionTokens",                             analyzeStats != null    &&  analyzeStats.getCompletionTokens() != null      ? analyzeStats.getCompletionTokens():0);
            records.put("successfulRequests",                           analyzeStats != null    &&  analyzeStats.getSuccessfulRequests() != null    ? analyzeStats.getSuccessfulRequests():0);
            records.put("totalCost",                                    analyzeStats != null    &&  analyzeStats.getTotalCost() != null             ? analyzeStats.getTotalCost():BigDecimal.ZERO);

            if(!version.equals(GeneralKeys.EXPECTED)){
                records.put(GeneralKeys.SUCCESS_RATE, analyzeStats != null ? analyzeStats.getSuccessRate() :BigDecimal.ZERO);
            }
            if(version.equals(GeneralKeys.EXPECTED)){
                List<DocumentAcceptedAnswer> documentAcceptedAnswers=acceptedAnswerRepository.getAcceptedAnswerList(item.getDocumentId());
                if(!documentAcceptedAnswers.isEmpty()) records.put(GeneralKeys.SUCCESS_RATE,BigDecimal.valueOf(100));
                else records.put(GeneralKeys.SUCCESS_RATE,BigDecimal.valueOf(0));
                for(DocumentAcceptedAnswer acceptedAnswer :documentAcceptedAnswers) {
                    String code = acceptedAnswer.getCode();
                    AnalyzeAnswerDTO analyzeAnswer = new AnalyzeAnswerDTO(acceptedAnswer.getShortAnswerType(), acceptedAnswer.getLongAnswer(), acceptedAnswer.getSummaryAnswer(), acceptedAnswer.getShortAnswerValue(), acceptedAnswer.isShortAnswerGenerated(),BigDecimal.valueOf(100));
                    records.put(code, analyzeAnswer);
                }
            }
            else{
                List<AnalyzeAnswer> answers = analyzeAnswerRepository.findByAnalyzeIdWithVersion(item.getId(),version);
                for(AnalyzeAnswer answer :answers){
                    String code=answer.getQuestionCode();
                    AnalyzeAnswerDTO analyzeAnswer=new AnalyzeAnswerDTO(answer.getShortAnswerType(), answer.getLongAnswer(),answer.getSummaryAnswer(),answer.getShortAnswerValue(),answer.isShortAnswerGenerated(),answer.getSuccessRate());
                    records.put(code,analyzeAnswer);
                }
            }
            recordsList.add(records);
        });
        analyzeListDTO.setRecords(recordsList);
        analyzeListDTO.setTotalRecords((int) analyzeList.getTotalElements());
        analyzeListDTO.setCurrent(pageNumber);
        analyzeListDTO.setNumPages(analyzeList.getTotalPages());
        analyzeListDTO.setPerPage(analyzeList.getContent().size());
        return analyzeListDTO;
    }

    private AnalyzeFilterDTO analyzeFilterDTO(String state,String status,LocalDateTime createdAtStart,LocalDateTime createdAtEnd,LocalDateTime updatedAtStart,LocalDateTime updatedAtEnd,LocalDateTime processStartedStart,LocalDateTime processStartedEnd,LocalDateTime processFinishedStart,LocalDateTime processFinishedEnd,String createUser,String updateUser,UUID documentId,String documentName,UUID documentTypeId,String isMetadataExist){
        AnalyzeFilterDTO analyzeFilterDTO = new AnalyzeFilterDTO();

        analyzeFilterDTO.setState(state);
        analyzeFilterDTO.setStatus(status);
        analyzeFilterDTO.setCreatedAtStart(createdAtStart);
        analyzeFilterDTO.setCreatedAtEnd(createdAtEnd);
        analyzeFilterDTO.setUpdatedAtStart(updatedAtStart);
        analyzeFilterDTO.setUpdatedAtEnd(updatedAtEnd);
        analyzeFilterDTO.setProcessStartedStart(processStartedStart);
        analyzeFilterDTO.setProcessStartedEnd(processStartedEnd);
        analyzeFilterDTO.setProcessFinishedStart(processFinishedStart);
        analyzeFilterDTO.setProcessFinishedEnd(processFinishedEnd);
        analyzeFilterDTO.setCreateUser(createUser);
        analyzeFilterDTO.setUpdateUser(updateUser);
        analyzeFilterDTO.setDocumentId(documentId);
        analyzeFilterDTO.setDocumentName(documentName);
        analyzeFilterDTO.setDocumentTypeId(documentTypeId);
        analyzeFilterDTO.setIsMetadataExist(isMetadataExist);

        return analyzeFilterDTO;
    }

    public List<AnalyzeQuestion> getQuestionList(){
        return analyzeQuestionRepository.findAll(Sort.by("orderNo"));
    }

    public AnalyzeJsonDTO getAnswer(String jsonResult) throws JsonProcessingException {
        return objectMapper.readValue(jsonResult, AnalyzeJsonDTO.class);
    }

    public void saveAnalyzeAnswers(String result,UUID analyzeId) throws JsonProcessingException {
        AnalyzeJsonDTO analyze = getAnswer(result);
        saveVersionSuccessRate(analyze,analyzeId);
        for (AnalyzeQueryDTO item : analyze.getQueries()) {
            for (AnalyzeAnswerJsonDTO it : item.getAnswers()) {
                saveQuestions(item);
                if(analyzeAnswerRepository.findByAnalyzeIdAndCode(analyzeId,item.getCode(),it.getVersion()) == null){
                    AnalyzeAnswer analyzeAnswer = new AnalyzeAnswer();

                    analyzeAnswer.setAnalyzeId(analyzeId);
                    analyzeAnswer.setDebugJson(objectMapper.writeValueAsString(it.getDebug()));
                    analyzeAnswer.setTagsJson(objectMapper.writeValueAsString(it.getTags()));
                    analyzeAnswer.setLongAnswer(it.getLongAnswer());
                    analyzeAnswer.setShortAnswerJson(objectMapper.writeValueAsString(it.getShortAnswer()));
                    analyzeAnswer.setMetaJson(objectMapper.writeValueAsString(it.getMeta()));
                    analyzeAnswer.setSummaryAnswer(it.getSummaryAnswer());
                    analyzeAnswer.setVersion(it.getVersion());
                    analyzeAnswer.setQuestionCode(item.getCode());
                    analyzeAnswer.setShortAnswerType(it.getShortAnswer().getType());
                    analyzeAnswer.setShortAnswerGenerated(it.getShortAnswer().isAnswerGenerated());
                    analyzeAnswer.setShortAnswerValue(objectMapper.writeValueAsString(it.getShortAnswer().getValue()));

                    Object successRateObj = it.getStats().get("success_rate");

                    controlObjForAnalyzeAnswers(successRateObj,analyzeAnswer,it);

                    analyzeAnswerRepository.save(analyzeAnswer);
                }
            }
        }
    }

    private void controlObjForAnalyzeAnswers(Object successRateObj, AnalyzeAnswer analyzeAnswer, AnalyzeAnswerJsonDTO it) {
        if (successRateObj instanceof Number) {
            analyzeAnswer.setSuccessRate(new BigDecimal(successRateObj.toString()));
        } else {
            analyzeAnswer.setSuccessRate(BigDecimal.ZERO);
        }
        Object lastedObj = it.getStats().get(GeneralKeys.LASTED);
        if (lastedObj instanceof Number) {
            analyzeAnswer.setLasted(new BigDecimal(lastedObj.toString()));
        } else {
            analyzeAnswer.setLasted(BigDecimal.ZERO);
        }
    }

    public List<AnalyzeDTO> analyzeDTOList(UUID documentId, String version){
        List<AnalyzeDTO> analyzeList = analyzeRepository.findAnalyzeDTOById(documentId,version);

        analyzeList.forEach(item->{
            AnalyzeStats analyzeStats   = analyzeStatsRepository.findByAnalyzeId(item.getId(),version);
            item.setTotalTokens(analyzeStats != null && analyzeStats.getTotalTokens() != null ? analyzeStats.getTotalTokens(): 0);
            item.setPromptTokens(analyzeStats != null  && analyzeStats.getPromptTokens() != null ? analyzeStats.getPromptTokens() :0);
            item.setCompletionTokens(analyzeStats != null && analyzeStats.getCompletionTokens() != null ? analyzeStats.getCompletionTokens():0);
            item.setSuccessfulRequests(analyzeStats != null && analyzeStats.getSuccessfulRequests() != null ? analyzeStats.getSuccessfulRequests():0);
            item.setTotalCost(analyzeStats != null  && analyzeStats.getTotalCost() != null ? analyzeStats.getTotalCost():BigDecimal.ZERO);
            if(item.getSuccessRate() == null) item.setSuccessRate(BigDecimal.ZERO);
        });

        analyzeList.sort((processDate1, processDate2) -> processDate2.getProcessStarted().compareTo(processDate1.getProcessStarted()));

        return analyzeList;
    }

    private void saveVersionSuccessRate(AnalyzeJsonDTO analyze,UUID analyzeId){
        List<Map<String,Object>> versionSuccessRateList = analyze.getVersionStats();
        versionSuccessRateList.forEach(item->{
            AnalyzeStats    analyzeStats    = new AnalyzeStats();
            Object          successRateObj  = item.get("success_rate");
            if (successRateObj instanceof Number) {
                analyzeStats.setSuccessRate(new BigDecimal(successRateObj.toString()));
            } else {
                analyzeStats.setSuccessRate(BigDecimal.ZERO);
            }
            Object lastedObj = item.get(GeneralKeys.LASTED);
            if (lastedObj instanceof Number) {
                analyzeStats.setLasted(new BigDecimal(lastedObj.toString()));
            } else {
                analyzeStats.setLasted(BigDecimal.ZERO);
            }
            Object totalCostObj = item.get("total_cost");
            if (lastedObj instanceof Number) {
                analyzeStats.setTotalCost(new BigDecimal(totalCostObj.toString()));
            } else {
                analyzeStats.setTotalCost(BigDecimal.ZERO);
            }
            analyzeStats.setVersion((String) item.get("version"));
            analyzeStats.setAnalyzeId(analyzeId);
            analyzeStats.setSuccessfulRequests((Integer) item.get("successful_requests"));
            analyzeStats.setCompletionTokens((Integer) item.get("completion_tokens"));
            analyzeStats.setPromptTokens((Integer) item.get("prompt_tokens"));
            analyzeStats.setTotalTokens((Integer) item.get("total_tokens"));
            try {
                analyzeStats.setMetaJson(objectMapper.writeValueAsString(item.get("meta")));
                analyzeStats.setTagsJson(objectMapper.writeValueAsString(item.get("tags")));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            analyzeStatsRepository.save(analyzeStats);
        });
    }

    public void saveQuestions(AnalyzeQueryDTO query){
        if(analyzeQuestionRepository.findByCode(query.getCode()) == null){
            AnalyzeQuestion analyzeQuestion=new AnalyzeQuestion();
            analyzeQuestion.setQuestionDesc(query.getQuestionDesc());
            analyzeQuestion.setQuestionTitle(query.getQuestionTitle());
            analyzeQuestion.setCode(query.getCode());
            analyzeQuestion.setDataType(query.getDataType());
            analyzeQuestion.setOrderNo(query.getId());
            analyzeQuestionRepository.save(analyzeQuestion);
        }
        else{
            AnalyzeQuestion existQuestion=analyzeQuestionRepository.findByCode(query.getCode());
            existQuestion.setQuestionDesc(query.getQuestionDesc());
            existQuestion.setQuestionTitle(query.getQuestionTitle());
            existQuestion.setDataType(query.getDataType());
            existQuestion.setOrderNo(query.getId());
            analyzeQuestionRepository.save(existQuestion);
        }
    }

    public String createAnalyze(UUID documentId,String username){
        AnalyzeDetail analyzeDetail = new AnalyzeDetail();
        analyzeDetail.setCreateUsername(username);
        analyzeDetail.setUpdateUsername(username);
        analyzeDetail.setDocumentId(documentId);
        analyzeDetail.setProcessStarted(LocalDateTime.now());

        return analyzeDetailRepository.save(analyzeDetail).getId().toString();
    }

    @Transactional
    public void updateAnalyze(UUID analyzeId,String resultAnalyze,UUID documentId,String username) throws JsonProcessingException {
        AnalyzeJsonDTO analyzeJsonDTO   = getAnswer(resultAnalyze);

        setPassiveAnalyze(documentId);
        saveAnalyzeAnswers(resultAnalyze,analyzeId);

        AnalyzeDetail analyzeDetail     = analyzeDetailRepository.findById(analyzeId).orElse(new AnalyzeDetail());

        analyzeDetail.setState("1");
        analyzeDetail.setStatus("1");
        analyzeDetail.setProcessFinished(LocalDateTime.now());
        analyzeDetail.setUpdatedAt(LocalDateTime.now());
        analyzeDetail.setResult(resultAnalyze);
        analyzeDetail.setUpdateUsername(username);
        Object successRateObj = analyzeJsonDTO.getStats().get("success_rate");
        if (successRateObj instanceof Number) {
            analyzeDetail.setSuccessRate(new BigDecimal(successRateObj.toString()));
        } else {
            analyzeDetail.setSuccessRate(BigDecimal.ZERO);
        }
        Object lastedObj = analyzeJsonDTO.getStats().get(GeneralKeys.LASTED);
        if (lastedObj instanceof Number) {
            analyzeDetail.setLasted(new BigDecimal(lastedObj.toString()));
        } else {
            analyzeDetail.setLasted(BigDecimal.ZERO);
        }
        analyzeDetail.setTopVersion((String) analyzeJsonDTO.getStats().get("top_version"));

        analyzeDetailRepository.save(analyzeDetail);
    }

    public void setPassiveAnalyze(UUID documentId){
        List<Analyze> analyzeList = analyzeRepository.findActiveListByDocumentId(documentId);
        analyzeList.forEach(item->{
            item.setState("0");
            item.setUpdatedAt(LocalDateTime.now());
            analyzeRepository.save(item);
        });
    }

    public AnalyzeDetail getAnalyzeDetail(UUID analyzeId , String version){
        AnalyzeDetail analyzeDetail     = analyzeDetailRepository.findById(analyzeId).orElseThrow(ContractWisorException.E005::new);
        AnalyzeStats analyzeStats       = analyzeStatsRepository.findByAnalyzeId(analyzeId,version);

        analyzeDetail.setTotalTokens(analyzeStats != null && analyzeStats.getTotalTokens() != null ? analyzeStats.getTotalTokens(): 0);
        analyzeDetail.setPromptTokens(analyzeStats != null  && analyzeStats.getPromptTokens() != null ? analyzeStats.getPromptTokens() :0);
        analyzeDetail.setCompletionTokens(analyzeStats != null && analyzeStats.getCompletionTokens() != null ? analyzeStats.getCompletionTokens():0);
        analyzeDetail.setSuccessfulRequests(analyzeStats != null && analyzeStats.getSuccessfulRequests() != null ? analyzeStats.getSuccessfulRequests():0);
        analyzeDetail.setTotalCost(analyzeStats != null  && analyzeStats.getTotalCost() != null ? analyzeStats.getTotalCost():BigDecimal.ZERO);

        if(!version.equals(GeneralKeys.EXPECTED)){
            analyzeDetail.setSuccessRate(analyzeStats != null ? analyzeStats.getSuccessRate() : BigDecimal.ZERO);
        }
        List<AnalyzeQuestionDTO> analyzeQuestions   =  analyzeQuestionRepository.listQuestionDTO();
        analyzeQuestions.forEach(item -> {
            if(version.equals(GeneralKeys.EXPECTED)){
                List<DocumentAcceptedAnswer> documentAcceptedAnswers=acceptedAnswerRepository.getAcceptedAnswerList(analyzeDetail.getDocumentId());
                if(!documentAcceptedAnswers.isEmpty()) analyzeDetail.setSuccessRate(BigDecimal.valueOf(100));
                else analyzeDetail.setSuccessRate(BigDecimal.valueOf(0));
                for(DocumentAcceptedAnswer acceptedAnswer :documentAcceptedAnswers) {
                    if(acceptedAnswer.getCode().equals(item.getCode())){
                        AnalyzeAnswerDTO analyzeAnswer = new AnalyzeAnswerDTO(acceptedAnswer.getShortAnswerType(), acceptedAnswer.getLongAnswer(), acceptedAnswer.getSummaryAnswer(), acceptedAnswer.getShortAnswerValue(), acceptedAnswer.isShortAnswerGenerated(),BigDecimal.valueOf(100));
                        item.setAnswer(analyzeAnswer);
                    }
                }
            } else {
                AnalyzeAnswerDTO answer =   analyzeAnswerRepository.findByAnalyzeIdAndCode(analyzeId,item.getCode(),version);
                item.setAnswer(answer);
            }
        });
        analyzeDetail.setQuestions(analyzeQuestions);
        analyzeDetail.setDocument(documentService.getDocById(analyzeDetail.getDocumentId()));
        if(analyzeDetail.getCreateUsername() != null)   analyzeDetail.setCreateUser(userService.findUserByUsername(analyzeDetail.getCreateUsername()));
        if(analyzeDetail.getUpdateUsername() != null)   analyzeDetail.setUpdateUser(userService.findUserByUsername(analyzeDetail.getUpdateUsername()));

        return analyzeDetail;
    }

    public List<Analyze> analyzeListByDocumentId(UUID documentId){
        return analyzeRepository.findAnalyzeListByDocumentId(documentId);
    }

    @Transactional
    public void deleteAnalyzeByDocumentId(UUID documentId){
        List<Analyze> analyzeList = analyzeRepository.findAnalyzeListByDocumentId(documentId);
        analyzeList.forEach(item->{
            analyzeAnswerRepository.deleteByAnalyzeId(item.getId());
            analyzeRepository.delete(item);
        });
    }

    private DocumentMetadataFilterDTO documentMetadataFilterDTO(String expiredContract,String company,String expiryDateFirst,String expiryDateLast){
        DocumentMetadataFilterDTO filterDTO=  new DocumentMetadataFilterDTO();

        filterDTO.setExpiredContract(expiredContract);
        filterDTO.setCompany(company);
        filterDTO.setExpiryDateFirst(expiryDateFirst);
        filterDTO.setExpiryDateLast(expiryDateLast);

        return filterDTO;
    }

    public void saveAcceptedAnswers(Map<String,Object> request){
        AnalyzeAcceptedJsonDTO      analyzeAcceptedJsonDTO  = new AnalyzeAcceptedJsonDTO();
        UUID                        documentId              = UUID.fromString(request.get("documentId").toString());

        analyzeAcceptedJsonDTO.setDocumentId(documentId);

        List<Map<String, Object>> queries   = (List<Map<String, Object>>) request.get("queries");
        if(queries != null) {
            queries.forEach(item -> {
                Map<String, Object> answer = (Map<String, Object>) item.get("answer");
                if(answer != null && item.get("code") != null && (answer.get(GeneralKeys.LONG_ANSWER) != null || answer.get(GeneralKeys.SHORT_ANSWER_VALUE) != null || answer.get(GeneralKeys.SHORT_ANSWER_TYPE) != null || answer.get("shortAnswerGenerated") != null || answer.get(GeneralKeys.SUMMARY_ANSWER) != null)){
                    DocumentAcceptedAnswer documentAcceptedAnswer = new DocumentAcceptedAnswer();

                    documentAcceptedAnswer.setId(acceptedAnswerRepository.existAcceptedAnswerId(documentId,item.get("code").toString()));
                    documentAcceptedAnswer.setDocumentId(documentId);
                    documentAcceptedAnswer.setCode(item.get("code").toString());

                    controlAndSaveAcceptedAnswers(answer,documentAcceptedAnswer);

                    acceptedAnswerRepository.save(documentAcceptedAnswer);
            }
            });
        }
    }

    private void controlAndSaveAcceptedAnswers(Map<String, Object> answer, DocumentAcceptedAnswer documentAcceptedAnswer ){
        try{
            if(answer.get(GeneralKeys.LONG_ANSWER) != null)  documentAcceptedAnswer.setLongAnswer(answer.get(GeneralKeys.LONG_ANSWER).toString());
            if(answer.get(GeneralKeys.SUMMARY_ANSWER) != null)  documentAcceptedAnswer.setSummaryAnswer(answer.get(GeneralKeys.SUMMARY_ANSWER).toString());

            documentAcceptedAnswer.setShortAnswerGenerated(true);

            if(answer.get(GeneralKeys.SHORT_ANSWER_TYPE) != null)   documentAcceptedAnswer.setShortAnswerType(answer.get(GeneralKeys.SHORT_ANSWER_TYPE).toString());
            if(answer.get(GeneralKeys.SHORT_ANSWER_VALUE) != null)  documentAcceptedAnswer.setShortAnswerValue(objectMapper.writeValueAsString(answer.get(GeneralKeys.SHORT_ANSWER_VALUE)));

            AnalyzeShortAnswerDTO analyzeShortAnswerDTO = new AnalyzeShortAnswerDTO();
            analyzeShortAnswerDTO.setAnswerGenerated(true);

            if(answer.get(GeneralKeys.SHORT_ANSWER_TYPE) != null)        analyzeShortAnswerDTO.setType(answer.get(GeneralKeys.SHORT_ANSWER_TYPE).toString());
            if(answer.get(GeneralKeys.SHORT_ANSWER_VALUE) != null)       analyzeShortAnswerDTO.setValue(answer.get(GeneralKeys.SHORT_ANSWER_VALUE));
            if(answer.get("text") != null)                               analyzeShortAnswerDTO.setText(answer.get("text").toString());

            documentAcceptedAnswer.setShortAnswerJson(objectMapper.writeValueAsString(analyzeShortAnswerDTO));
        }  catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public AnalyzeAcceptedJsonDTO getAcceptedAnswers(UUID documentId){
        AnalyzeAcceptedJsonDTO              analyzeAcceptedJsonDTO      = new AnalyzeAcceptedJsonDTO();

        analyzeAcceptedJsonDTO.setDocumentId(documentId);

        List<AnalyzeAcceptedQueryDTO>       queries                     = new ArrayList<>();
        List<DocumentAcceptedAnswer>        documentAcceptedAnswers     = acceptedAnswerRepository.getAcceptedAnswerList(documentId);
        List<AnalyzeQuestion>               questions                   = analyzeQuestionRepository.findAll();

        questions.forEach(item->{
            DocumentAcceptedAnswer documentAcceptedAnswer                   = new DocumentAcceptedAnswer();
            if(documentAcceptedAnswers.stream().anyMatch(answer -> answer.getCode().equals(item.getCode()))) documentAcceptedAnswer =   documentAcceptedAnswers.stream().filter(answer->answer.getCode().equals(item.getCode())).collect(Collectors.toList()).get(0);
            AnalyzeAcceptedQueryDTO     analyzeAcceptedQueryDTO             = new AnalyzeAcceptedQueryDTO();
            AnalyzeAnswerAcceptedJsonDTO analyzeAnswerAcceptedJsonDTO       = new AnalyzeAnswerAcceptedJsonDTO();

            analyzeAnswerAcceptedJsonDTO.setLongAnswer(documentAcceptedAnswer.getLongAnswer());
            analyzeAnswerAcceptedJsonDTO.setSummaryAnswer(documentAcceptedAnswer.getSummaryAnswer());
            analyzeAnswerAcceptedJsonDTO.setShortAnswerType(documentAcceptedAnswer.getShortAnswerType());
            analyzeAnswerAcceptedJsonDTO.setShortAnswerGenerated(documentAcceptedAnswer.isShortAnswerGenerated());
            try {
                if(documentAcceptedAnswer.getShortAnswerType() != null)analyzeAnswerAcceptedJsonDTO.setShortAnswerValue(objectMapper.readValue(documentAcceptedAnswer.getShortAnswerValue(),Object.class));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            analyzeAcceptedQueryDTO.setCode(item.getCode());
            analyzeAcceptedQueryDTO.setAnswer(analyzeAnswerAcceptedJsonDTO);
            analyzeAcceptedQueryDTO.setQuestionDataType(item.getDataType());
            analyzeAcceptedQueryDTO.setId(item.getId());
            analyzeAcceptedQueryDTO.setQuestionTitle(item.getQuestionTitle());
            analyzeAcceptedQueryDTO.setQuestionDesc(item.getQuestionDesc());
            queries.add(analyzeAcceptedQueryDTO);
        });
        analyzeAcceptedJsonDTO.setQueries(queries);

        return analyzeAcceptedJsonDTO;
    }

    public DocumentAnalyzeListDTO getDocumentWithAnalyzeList(String version,int pageNumber,int pageSize,String state,String status,LocalDateTime createdAtStart,LocalDateTime createdAtEnd,LocalDateTime updatedAtStart,LocalDateTime updatedAtEnd,LocalDateTime processStartedStart,LocalDateTime processStartedEnd,LocalDateTime processFinishedStart,LocalDateTime processFinishedEnd,String createUser,String updateUser,UUID documentId,String documentName,UUID documentTypeId,String isMetadataExist,String expiredContract,String company,String expiryDateFirst,String expiryDateLast) {
        AnalyzeFilterDTO                    filterDTO                           = analyzeFilterDTO(state, status, createdAtStart, createdAtEnd, updatedAtStart, updatedAtEnd, processStartedStart, processStartedEnd, processFinishedStart, processFinishedEnd, createUser,updateUser,documentId,documentName,documentTypeId,isMetadataExist);
        DocumentMetadataFilterDTO           documentMetadataFilterDTO           = documentMetadataFilterDTO(expiredContract, company, expiryDateFirst, expiryDateLast);
        AnalyzeSpecification                analyzeSpecification                = new AnalyzeSpecification(filterDTO,documentMetadataFilterDTO,true);
        Sort                                sort                                = Sort.by(Sort.Order.desc(GeneralKeys.PROCESS_STARTED));
        Pageable                            pageable                            = PageRequest.of(pageNumber -1, Math.min(pageSize, 100), sort);
        Page<Analyze>                       analyzeList                         = analyzeRepository.findAll(analyzeSpecification,pageable);
        List<DocumentAnalyzeDTO>            documentAnalyzeDTOList              = analyzeList.getContent().stream()
                                                                                                        .map(analyze -> convertToDTO(analyze.getDocument(),version))
                                                                                                        .collect(Collectors.toList());
        DocumentAnalyzeListDTO documentListDTO                                  = new DocumentAnalyzeListDTO();

        documentListDTO.setRecords(documentAnalyzeDTOList);
        documentListDTO.setTotalRecords((int) analyzeList.getTotalElements());
        documentListDTO.setCurrent(pageNumber);
        documentListDTO.setNumPages(analyzeList.getTotalPages());
        documentListDTO.setPerPage(analyzeList.getContent().size());

        return documentListDTO;
    }

    private DocumentAnalyzeDTO convertToDTO(Document document,String version) {
        return new DocumentAnalyzeDTO(
                document.getId(),
                document.getPageCount(),
                document.getName(),
                document.getSize(),
                document.getState(),
                document.getCreateDate(),
                document.getCreateTime(),
                document.getUpdateDate(),
                document.getUpdateTime(),
                document.getStatus(),
                document.getDocumentFileType(),
                document.getCreateUser(),
                document.getUpdateUser(),
                analyzeDTOList(document.getId(),version),
                document.getMetadata(),
                document.getDocumentType()
        );
    }
}
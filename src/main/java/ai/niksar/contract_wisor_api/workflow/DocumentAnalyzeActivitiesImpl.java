package ai.niksar.contract_wisor_api.workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.service.AnalyzeService;
import ai.niksar.contract_wisor_api.service.DocumentService;

import java.util.UUID;

@Component
public class DocumentAnalyzeActivitiesImpl implements DocumentAnalyzeActivities {

    private final AnalyzeService analyzeService;
    private final DocumentService documentService;

    @Autowired
    public DocumentAnalyzeActivitiesImpl(AnalyzeService analyzeService,DocumentService documentService) {
        this.analyzeService     = analyzeService;
        this.documentService    = documentService;
    }

    @Override
    public String createAnalyze(UUID documentId,String username){
        return analyzeService.createAnalyze(documentId,username);
    }

    @Override
    public void updateAnalyze(UUID analyzeId,String result,UUID documentId,String username) throws JsonProcessingException {
        analyzeService.updateAnalyze(analyzeId,result,documentId,username);
    }

    @Override
    public String updateDocumentState(UUID documentId,String state){
        return documentService.updateDocumentState(documentId,state).getState();
    }

    @Override
    public String updateDocumentStateAndAnalyzeState(UUID documentId,String state, String analyzeState){
        return documentService.updateDocumentStateAndAnalyzeState(documentId,state, analyzeState).getState();
    }

    @Override
    public String updateDocumentAnalyzeState(UUID documentId,String state){
        return documentService.updateDocumentAnalyzeState(documentId,state).getState();
    }
}

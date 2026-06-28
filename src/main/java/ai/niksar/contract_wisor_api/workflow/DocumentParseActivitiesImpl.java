package ai.niksar.contract_wisor_api.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.service.DocumentService;

import java.util.UUID;

@Component
public class DocumentParseActivitiesImpl implements DocumentParseActivities {

    private final DocumentService documentService;


    @Autowired
    public DocumentParseActivitiesImpl(DocumentService documentService) {
        this.documentService = documentService;
    }

    @Override
    public String updateDocumentParseState(UUID documentId,String state){
        return documentService.updateDocumentParseState(documentId,state).getState();
    }

    @Override
    public String updateDocumentState(UUID documentId,String state){
        return documentService.updateDocumentState(documentId,state).getState();
    }

    @Override
    public String updateDocumentAndParseState(UUID documentId,String state, String parseState){
        return documentService.updateDocumentAndParseState(documentId,state, parseState).getState();
    }
}

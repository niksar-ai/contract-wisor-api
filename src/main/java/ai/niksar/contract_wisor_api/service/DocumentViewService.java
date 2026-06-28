package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.model.DocumentView;
import ai.niksar.contract_wisor_api.repository.DocumentViewRepository;
import ai.niksar.contract_wisor_api.util.Util;

import java.util.UUID;

@Service
public class DocumentViewService {
    @Autowired
    private DocumentViewRepository documentViewRepository;

    public void  saveDocumentView(UUID documentId){
        String username= SecurityContextHolder.getContext().getAuthentication().getName();
        DocumentView documentView=new DocumentView();
        documentView.setViewDocumentId(documentId);
        documentView.setViewDate(Util.getRealDate());
        documentView.setViewTime(Util.getRealTime());
        documentView.setViewUser(username);
        documentViewRepository.save(documentView);
    }
}

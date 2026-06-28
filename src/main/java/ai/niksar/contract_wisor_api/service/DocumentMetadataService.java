package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.model.DocumentMetadata;
import ai.niksar.contract_wisor_api.repository.DocumentMetadataRepository;
import ai.niksar.contract_wisor_api.util.Util;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentMetadataService {

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    // Save the DocumentMetadata record
    public DocumentMetadata saveDocumentMetadata(DocumentMetadata documentMetadata, UUID documentId) {
        DocumentMetadata existDocumentMetadata = getDocumentMetadataByDocumentId(documentId);
        documentMetadata.setDocumentId(documentId);
        if(existDocumentMetadata != null){
            documentMetadata.setId(existDocumentMetadata.getId());
            documentMetadata.setStatus(existDocumentMetadata.getStatus());
            documentMetadata.setCreateDate(existDocumentMetadata.getCreateDate());
            documentMetadata.setCreateTime(existDocumentMetadata.getCreateTime());
        } else {
            documentMetadata.setStatus("1");
            documentMetadata.setCreateDate(Util.getRealDate());
            documentMetadata.setCreateTime(Util.getRealTime());
        }
        if(documentMetadata.getExpiryDate() != null && documentMetadata.getExpiryDate().contains("-")){
            documentMetadata.setExpiryDate(documentMetadata.getExpiryDate().replace("-",""));
        }
        return documentMetadataRepository.save(documentMetadata);
    }

    // List all DocumentMetadata records
    public List<DocumentMetadata> getAllDocumentMetadata() {
        return documentMetadataRepository.findAll();
    }

    // List the DocumentMetadata records for a specific documentId
    public DocumentMetadata getDocumentMetadataByDocumentId(UUID documentId) {
        return documentMetadataRepository.findByDocumentId(documentId);
    }
    public void delete(DocumentMetadata metadata){
        documentMetadataRepository.delete(metadata);
    }

    @Transactional
    public void deleteByDocumentId(UUID documentId){
        documentMetadataRepository.deleteByDocumentId(documentId);
        documentMetadataRepository.flush();
    }
}

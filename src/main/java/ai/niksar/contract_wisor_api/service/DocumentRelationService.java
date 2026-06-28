package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ai.niksar.contract_wisor_api.dto.DocumentFilterDTO;
import ai.niksar.contract_wisor_api.dto.DocumentMetadataFilterDTO;
import ai.niksar.contract_wisor_api.model.DocumentRelation;
import ai.niksar.contract_wisor_api.repository.DocumentRelationRepository;
import ai.niksar.contract_wisor_api.specification.DocumentRelationSpecification;

import java.util.List;
import java.util.UUID;

@Service
public class DocumentRelationService {

    @Autowired
    private DocumentRelationRepository documentRelationRepository;
    @Autowired
    @Lazy
    private DocumentService documentService;

    public List<DocumentRelation> getDocumentRelationList(DocumentFilterDTO filterDTO, DocumentMetadataFilterDTO documentMetadataFilterDTO) {
        Specification<DocumentRelation> spec = new DocumentRelationSpecification(filterDTO, documentMetadataFilterDTO);
        return documentRelationRepository.findAll(spec);
    }

    public Page<DocumentRelation> getRelationsByParentIdPageable(UUID parentId,Pageable pageable) {
        return documentRelationRepository.findByParentIdPageable(parentId,pageable);
    }
    public List<DocumentRelation> getRelationsByParentId(UUID parentId) {
        return documentRelationRepository.findByParentId(parentId);
    }
    public List<DocumentRelation> getRelationsByChildId(UUID childId){
        return documentRelationRepository.findByChildId(childId);
    }
    public DocumentRelation saveDocumentRelation(DocumentRelation documentRelation) {
        return documentRelationRepository.save(documentRelation);
    }
    @Transactional
    public void deleteDocumentRelation(DocumentRelation documentRelation){
        documentRelationRepository.delete(documentRelation);
        documentRelationRepository.flush();
    }

    public DocumentRelation getRelationById(UUID id){
        return documentRelationRepository.findById(id).get();
    }
    public DocumentRelation getRelationsByChildIdAndParentId(UUID parentId,UUID childId){
        return documentRelationRepository.findByParentIdAndChild_Id(parentId,childId);
    }
}


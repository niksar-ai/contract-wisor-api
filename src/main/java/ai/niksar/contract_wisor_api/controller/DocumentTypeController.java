package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.DocumentTypeCountDTO;
import ai.niksar.contract_wisor_api.dto.DocumentTypeSizeDTO;
import ai.niksar.contract_wisor_api.model.DocumentType;
import ai.niksar.contract_wisor_api.repository.DocumentRepository;
import ai.niksar.contract_wisor_api.repository.DocumentTypeRepository;
import ai.niksar.contract_wisor_api.service.DocumentRelationService;
import ai.niksar.contract_wisor_api.service.DocumentTypeService;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/document-type")
public class DocumentTypeController {

    @Autowired
    private DocumentTypeService documentTypeService;
    @Autowired
    private DocumentTypeRepository documentTypeRepository;
    @Autowired
    private DocumentRelationService documentRelationService;
    @Autowired
    private DocumentRepository documentRepository;

    @GetMapping("/tree")
    public List<DocumentType> getDocumentTypeTree() {
        return documentTypeService.getDocumentTypeTree();
    }

    @PostMapping("/save")
    public ResponseEntity<DocumentType> saveDocumentType(@RequestBody DocumentType documentType) {
        List<DocumentType> children = documentType.getChildren();
        if(documentType.getId() != null){
            documentTypeService.saveDocumentType(documentType);
        }
        if(!children.isEmpty()){
            children.forEach(documentTypeService::saveDocumentType);
        }
        return ResponseEntity.ok(documentType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocumentType(@PathVariable UUID id) {
        documentTypeService.deleteDocumentType(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocumentType> updateDocumentType(@PathVariable UUID id, @RequestBody DocumentType documentTypeRequest) {
        DocumentType updatedDocumentType = documentTypeService.updateDocumentType(id, documentTypeRequest.getName(), documentTypeRequest.getParentId());
        return ResponseEntity.ok(updatedDocumentType);
    }

    @GetMapping("/list/top-document-count")
    public ResponseEntity<List<DocumentTypeCountDTO>> getSortedDocumentList(@RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(documentTypeService.typeListWithDocCount(size));
    }

    @GetMapping("/list/top-document-size")
    public ResponseEntity<List<DocumentTypeSizeDTO>> getSortedDocument(@RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(documentTypeService.getDocumentTypeWithTotalSize(size));
    }

    @GetMapping("/list")
    public ResponseEntity<?> getDocumentTypeList() {
        return ResponseEntity.ok(documentTypeService.documentTypeList());
    }

}
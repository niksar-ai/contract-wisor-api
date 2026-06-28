package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.dto.*;
import ai.niksar.contract_wisor_api.model.DocumentComment;
import ai.niksar.contract_wisor_api.model.DocumentContent;
import ai.niksar.contract_wisor_api.model.DocumentDetail;
import ai.niksar.contract_wisor_api.model.DocumentMetadata;
import ai.niksar.contract_wisor_api.service.DocumentService;
import ai.niksar.contract_wisor_api.util.ActionCode;
import ai.niksar.contract_wisor_api.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api/document")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/list")
    public ResponseEntity<Object> getDocumentList(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size,@RequestParam(required = false) String status ,@RequestParam(required = false) String state ,@RequestParam(required = false) String name ,@RequestParam(required = false) String firstUpdateDate ,@RequestParam(required = false) String lastUpdateDate ,@RequestParam(required = false) String firstCreateDate ,@RequestParam(required = false) String lastCreateDate ,@RequestParam(required = false) Long sizeMin ,@RequestParam(required = false) Long sizeMax ,@RequestParam(required = false) String isMetadataExist,@RequestParam(required = false) String createUser, @RequestParam(required = false) String updateUser,@RequestParam(required = false) UUID documentTypeId,@RequestParam(required = false) String expiredContract,@RequestParam(required = false) String description,@RequestParam(required = false) String expiryDateFirst,@RequestParam(required = false) String expiryDateLast,@RequestParam(required = false) String company)  {
        return ResponseEntity.ok(documentService.getDocumentList(page,size,status,state,name,firstUpdateDate,lastUpdateDate,firstCreateDate,lastCreateDate,sizeMin,sizeMax,isMetadataExist,createUser,updateUser,documentTypeId, expiredContract, description, company, expiryDateFirst, expiryDateLast));
    }

    @GetMapping("/statistics")
    public ResponseEntity<Object> getDocumentStatistics() {
        try{
            return ResponseEntity.ok(documentService.getDocumentStatistics());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/list/last-update")
    public ResponseEntity<Object> getSortedDocumentList(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size)  {
        return ResponseEntity.ok(documentService.getSortedDocumentList(page,size));
    }

    @GetMapping("/tree")
    public ResponseEntity<List<NodeDTO>> getDocumentBuildTree(@RequestParam(required = false) String status ,@RequestParam(required = false) String state ,@RequestParam(required = false) String name ,@RequestParam(required = false) String firstUpdateDate ,@RequestParam(required = false) String lastUpdateDate ,@RequestParam(required = false) String firstCreateDate ,@RequestParam(required = false) String lastCreateDate ,@RequestParam(required = false) Long sizeMin ,@RequestParam(required = false) Long sizeMax ,@RequestParam(required = false) String isMetadataExist,@RequestParam(required = false) String createUser, @RequestParam(required = false) String updateUser,@RequestParam(required = false) UUID documentTypeId,@RequestParam(required = false) String expiredContract,@RequestParam(required = false) String description,@RequestParam(required = false) String expiryDateFirst,@RequestParam(required = false) String expiryDateLast,@RequestParam(required = false) String company) {
        return ResponseEntity.ok(documentService.getDocumentTree(status,state,name,firstUpdateDate,lastUpdateDate,firstCreateDate,lastCreateDate,sizeMin,sizeMax,isMetadataExist,createUser,updateUser,documentTypeId, expiredContract, description, company, expiryDateFirst, expiryDateLast));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocumentDetail> getDocumentById(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocumentById(id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_RELATION_SAVE)
    @PostMapping("/{id}/relation/save")
    public ResponseEntity<Object> saveRelation(@RequestBody DocumentChildDTO documentChildDTO,@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.saveDocumentRelation(documentChildDTO, id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_RELATION_DELETE)
    @DeleteMapping("/{id}/relation/{childId}")
    public ResponseEntity<Object> deleteRelation(@PathVariable UUID childId,@PathVariable UUID id){
       return ResponseEntity.ok(documentService.deleteDocumentRelation(id,childId));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_RELATION_VIEW)
    @GetMapping("/{id}/relation/list")
    public ResponseEntity<Object> getRelationList(@PathVariable UUID id,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(documentService.getRelationList(id,page,size));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_RELATION_VIEW_TREE)
    @GetMapping("/{id}/relation/tree")
    public ResponseEntity<Object> getDocumentRelationTree(@PathVariable UUID id) {
        return ResponseEntity.ok(documentService.getDocumentRelationTree(id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_METADATA_SAVE)
    @PostMapping("/{id}/metadata/save")
    public ResponseEntity<Object> saveMetadata(@RequestBody DocumentMetadata documentMetadata, @PathVariable UUID id){
        return ResponseEntity.ok(documentService.saveMetadata(documentMetadata,id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteDocument(@PathVariable UUID id){
        return ResponseEntity.ok(documentService.deleteDocument(id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_PARSE)
    @PostMapping("/{id}/parse")
    public ResponseEntity<Object> parseDocument(@PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(documentService.startDocumentParseWorkflow(id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_ANALYZE)
    @PostMapping("/{id}/analyze")
    public ResponseEntity<Object> analyzeDocument(@PathVariable UUID id) throws Exception {
        return ResponseEntity.ok(documentService.analyzeDocument(id));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Object> downloadDocument(@PathVariable UUID id) {
        DocumentContent document = documentService.getDocumentContent(id);
        return ResponseEntity.ok(document.getContent());
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_DOWNLOAD)
    @GetMapping("/{id}/json-download")
    public ResponseEntity<Object> downloadDocumentTextContent(@PathVariable UUID id) {
        DocumentContent document = documentService.getDocumentTextContent(id);
        return ResponseEntity.ok(document.getTextContent());
    }

    @GetMapping("/list/last-view")
    public ResponseEntity<Object> getSortedDocumentViewList(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(documentService.getSortedDocumentViewList(page,size));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_COMMENT_SAVE)
    @PostMapping("/{id}/comment/save")
    public ResponseEntity<Object> saveComment(@RequestBody DocumentComment documentComment, @PathVariable UUID id){
        return ResponseEntity.ok(documentService.saveComment(documentComment,id));
    }

    @GetMapping("/{id}/comment/list")
    public ResponseEntity<Object> listComment(@PathVariable UUID id){
        return ResponseEntity.ok(documentService.listComment(id));
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_COMMENT_DELETE)
    @DeleteMapping("/{id}/comment/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable UUID commentId){
        return ResponseEntity.ok(documentService.deleteComment(commentId));
    }

    @GetMapping("/{id}/favourited-users")
    public ResponseEntity<Object> listFavUsers(@PathVariable UUID id){
        return ResponseEntity.ok(documentService.getDocumentFavoritedUsers(id));
    }

    @PostMapping("/update-state")
    public ResponseEntity<Object> updateDocumentState(){
        return ResponseEntity.ok(documentService.updateDocumentStates());
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_COMPLETE)
    @PostMapping("/{id}/complete")
    public ResponseEntity<Object> documentComplete(@PathVariable UUID id)  {
        try{
            documentService.updateDocumentState(id, Constants.DocumentState.COMPLETED);
            return ResponseEntity.ok("Document has been set to completed status.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while updating the document status. \nError: " + e.getMessage());
        }
    }

    @ActionCode(Constants.ActionCodes.DOCUMENT_REOPEN)
    @PostMapping("/{id}/reopen")
    public ResponseEntity<Object> reopenDocument(@PathVariable UUID id)  {
        try{
            documentService.reopenDocument(id);
            return ResponseEntity.ok("The document can now be reprocessed.");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An error occurred while reprocessing the document. \nError: " + e.getMessage());
        }
    }
}
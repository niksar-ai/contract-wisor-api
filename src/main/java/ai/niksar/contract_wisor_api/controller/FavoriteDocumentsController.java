package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.service.DocumentFavouriteService;

import java.util.UUID;

@RestController
@RequestMapping("/api/favorite")
public class FavoriteDocumentsController {
    @Autowired
    DocumentFavouriteService documentFavouriteService;

    @PostMapping("/{documentId}")
    public ResponseEntity<Object> saveFavDocument(@PathVariable UUID documentId) throws Exception {
        return ResponseEntity.ok(documentFavouriteService.saveDocumentFav(documentId));
    }

    @GetMapping("/list")
    public ResponseEntity<Object> listFavDocumentsByUser(){
        return ResponseEntity.ok(documentFavouriteService.listFavDocumentsByUsername());
    }

    @GetMapping("/list-by-id/{documentId}")
    public ResponseEntity<Object> listFavDocuments(@PathVariable UUID documentId){
        return ResponseEntity.ok(documentFavouriteService.listUsersByDocId(documentId));
    }

    @DeleteMapping("/{documentId}")
    public ResponseEntity<Object> deleteFavDocument(@PathVariable UUID documentId){
        return ResponseEntity.ok(documentFavouriteService.deleteFav(documentId));
    }
}

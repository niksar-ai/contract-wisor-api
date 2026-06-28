package ai.niksar.contract_wisor_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.service.AnalyzeService;
import ai.niksar.contract_wisor_api.util.Constants;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/analyze")
public class AnalyzeController {
    @Autowired
    AnalyzeService analyzeService;

    @GetMapping("/list")
    public ResponseEntity<Object> listAnalyze(
            @RequestParam(defaultValue = Constants.AnalyzeAnswerVersions.DEFAULT) String version,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String state,
            @RequestParam(required = false, defaultValue = "1") String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processStartedStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processStartedEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processFinishedStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processFinishedEnd,
            @RequestParam(required = false) String createUser,
            @RequestParam(required = false) String updateUser,
            @RequestParam(required = false) UUID documentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID documentTypeId,
            @RequestParam(required = false) String isMetadataExist,
            @RequestParam(required = false) String expiredContract,
            @RequestParam(required = false) String expiryDateFirst,
            @RequestParam(required = false) String expiryDateLast,
            @RequestParam(required = false) String company){
            return ResponseEntity.ok(analyzeService.getAnalysis(version,page,size,state,status,createdAtStart,createdAtEnd,updatedAtStart,updatedAtEnd,processStartedStart,processStartedEnd,processFinishedStart,processFinishedEnd,createUser,updateUser,documentId,name,documentTypeId,isMetadataExist, expiredContract, company, expiryDateFirst, expiryDateLast));
    }

    @GetMapping("/question/list")
    public ResponseEntity<Object> listAnalyzeQuestion(){
        return ResponseEntity.ok(analyzeService.getQuestionList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getAnalyzeDetail(@PathVariable UUID id ,@RequestParam(defaultValue = Constants.AnalyzeAnswerVersions.DEFAULT) String version){
        return ResponseEntity.ok(analyzeService.getAnalyzeDetail(id,version));
    }

    @PostMapping("/expected-answers")
    public void saveAcceptedAnswer(@RequestBody Map<String,Object> jsonInput) {
        analyzeService.saveAcceptedAnswers(jsonInput);
    }

    @GetMapping("/expected-answers")
    public ResponseEntity<Object> getAcceptedAnswers(@RequestParam UUID documentId){
        return ResponseEntity.ok(analyzeService.getAcceptedAnswers(documentId));
    }

    @GetMapping("/tree")
    public ResponseEntity<Object> listAnalyzeTree(
            @RequestParam(defaultValue = Constants.AnalyzeAnswerVersions.DEFAULT) String version,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String state,
            @RequestParam(required = false, defaultValue = "1") String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAtStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime updatedAtEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processStartedStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processStartedEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processFinishedStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime processFinishedEnd,
            @RequestParam(required = false) String createUser,
            @RequestParam(required = false) String updateUser,
            @RequestParam(required = false) UUID documentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) UUID documentTypeId,
            @RequestParam(required = false) String isMetadataExist,
            @RequestParam(required = false) String expiredContract,
            @RequestParam(required = false) String expiryDateFirst,
            @RequestParam(required = false) String expiryDateLast,
            @RequestParam(required = false) String company){
            return ResponseEntity.ok(analyzeService.getDocumentWithAnalyzeList(version,page,size,state,status,createdAtStart,createdAtEnd,updatedAtStart,updatedAtEnd,processStartedStart,processStartedEnd,processFinishedStart,processFinishedEnd,createUser,updateUser,documentId,name,documentTypeId,isMetadataExist, expiredContract, company, expiryDateFirst, expiryDateLast));
    }
}
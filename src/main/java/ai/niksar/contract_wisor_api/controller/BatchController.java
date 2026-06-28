package ai.niksar.contract_wisor_api.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ai.niksar.contract_wisor_api.batch.BatchManager;
import ai.niksar.contract_wisor_api.model.BatchDefinition;
import ai.niksar.contract_wisor_api.repository.BatchDefinitionRepository;
import ai.niksar.contract_wisor_api.service.BatchLogService;
import ai.niksar.contract_wisor_api.batch.BatchService;
import ai.niksar.contract_wisor_api.util.Constants;

import java.util.UUID;

@RestController
@RequestMapping("/api/batch")
public class BatchController {

    @Autowired
    private BatchDefinitionRepository batchDefinitionRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BatchLogService batchLogService;

    @Autowired
    private BatchManager batchManager;

    @PostMapping("/trigger/{batchId}")
    public ResponseEntity<String> triggerBatch(@PathVariable UUID batchId) {
        BatchDefinition batch = batchDefinitionRepository.findById(batchId).orElse(null);
        if (batch == null) {
            return ResponseEntity.badRequest().body("Batch not found with ID: " + batchId);
        }
        String serviceName = batch.getServiceName();
        try {
            String basePackage = Constants.BATCH_BASE_PATH;
            String fullClassName = basePackage + serviceName;
            BatchService batchService = (BatchService) applicationContext.getBean(Class.forName(fullClassName));
            batchService.execute();
            batchLogService.logBatchSuccess(batchId);
            return ResponseEntity.ok("Batch triggered successfully for service: " + serviceName);
        } catch (ClassNotFoundException | BeansException e) {
            batchLogService.logBatchFailure(batchId, e.getMessage());
            return ResponseEntity.badRequest().body("Error triggering batch: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error triggering batch: " + e.getMessage());
        }
    }

    @PostMapping("/reload")
    public ResponseEntity<String> reloadBatchDefinitions() {
        try {
            batchManager.initializeBatches();
            return ResponseEntity.ok("Batch definitions reloaded successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error reloading batch definitions: " + e.getMessage());
        }
    }
}
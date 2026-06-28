package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.model.BatchDefinition;
import ai.niksar.contract_wisor_api.repository.BatchDefinitionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BatchDefinitionService {

    @Autowired
    private BatchDefinitionRepository batchDefinitionRepository;

    public BatchDefinition createBatch(BatchDefinition batchDefinition) {
        Optional<BatchDefinition> existingBatch = batchDefinitionRepository.findByServiceName(batchDefinition.getServiceName());
        if (existingBatch.isPresent()) {
            throw new IllegalArgumentException("Batch with service name: " + batchDefinition.getServiceName() + " already exists.");
        }

        batchDefinition.setCreatedAt(LocalDateTime.now());
        batchDefinition.setUpdatedAt(LocalDateTime.now());

        return batchDefinitionRepository.save(batchDefinition);
    }

    public BatchDefinition updateBatch(UUID batchId, BatchDefinition batchDetails) {
        Optional<BatchDefinition> optionalBatch = batchDefinitionRepository.findById(batchId);

        if (optionalBatch.isPresent()) {
            BatchDefinition batchDefinition = optionalBatch.get();
            batchDefinition.setBatchName(batchDetails.getBatchName());
            batchDefinition.setServiceName(batchDetails.getServiceName());
            batchDefinition.setCronTime(batchDetails.getCronTime());
            batchDefinition.setMaxRetryCount(batchDetails.getMaxRetryCount());
            batchDefinition.setRetryInterval(batchDetails.getRetryInterval());
            batchDefinition.setStatus(batchDetails.getStatus());

            batchDefinition.setUpdatedAt(LocalDateTime.now());

            return batchDefinitionRepository.save(batchDefinition);
        } else {
            throw new IllegalArgumentException("Batch with ID: " + batchId + " not found.");
        }
    }

    public void deleteBatch(UUID batchId) {
        Optional<BatchDefinition> batchDefinition = batchDefinitionRepository.findById(batchId);
        if (batchDefinition.isPresent()) {
            batchDefinitionRepository.deleteById(batchId);
        } else {
            throw new IllegalArgumentException("Batch with ID: " + batchId + " not found.");
        }
    }

    public BatchDefinition getBatchById(UUID batchId) {
        return batchDefinitionRepository.findById(batchId).orElseThrow(() ->
                new IllegalArgumentException("Batch with ID: " + batchId + " not found."));
    }

    public List<BatchDefinition> getAllBatches() {
        return batchDefinitionRepository.findAll();
    }
}

package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.model.BatchLog;
import ai.niksar.contract_wisor_api.repository.BatchLogRepository;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BatchLogService {

    @Autowired
    private BatchLogRepository batchLogRepository;

    public BatchLog logBatchStart(UUID batchId) {
        BatchLog log = new BatchLog();
        log.setBatchId(batchId);
        log.setLogTime(LocalDateTime.now());
        log.setStatus(BatchLogStatus.PROCESSING);
        log.setStartTime(LocalDateTime.now());
        return batchLogRepository.save(log);
    }

    public void logBatchSuccess(UUID batchId) {
        BatchLog log = batchLogRepository.findTopByBatchIdOrderByStartTimeDesc(batchId).orElse(null);
        if (log != null) {
            log.setEndTime(LocalDateTime.now());
            log.setStatus(BatchLogStatus.SUCCESS);
            log.setDuration(java.time.Duration.between(log.getStartTime(), log.getEndTime()).toMillis());
            batchLogRepository.save(log);
        } else {
            logBatchFailure(batchId, "No log record found for the success operation.");
        }
    }

    public void logBatchFailure(UUID batchId, String errorMessage) {
        BatchLog log = batchLogRepository.findTopByBatchIdOrderByStartTimeDesc(batchId).orElse(null);

        if (log == null) {
            log = new BatchLog();
            log.setBatchId(batchId);
            log.setStartTime(LocalDateTime.now());
            log.setLogTime(LocalDateTime.now());
            log.setStatus(BatchLogStatus.FAILURE);
        }

        log.setEndTime(LocalDateTime.now());
        log.setErrorMessage(errorMessage);
        log.setDuration(java.time.Duration.between(log.getStartTime(), log.getEndTime()).toMillis());

        batchLogRepository.save(log);
    }
}

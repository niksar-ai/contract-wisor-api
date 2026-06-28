package ai.niksar.contract_wisor_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.model.ThreadLog;
import ai.niksar.contract_wisor_api.repository.ThreadLogRepository;
import ai.niksar.contract_wisor_api.repository.BatchDefinitionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ThreadLogService {

    @Autowired
    private ThreadLogRepository threadLogRepository;

    @Autowired
    private BatchDefinitionRepository batchDefinitionRepository;

    public void logThreadStart(UUID batchId, String threadName) {
        ThreadLog log = new ThreadLog();
        log.setBatchId(batchId);
        log.setThreadName(threadName);
        log.setStartTime(LocalDateTime.now());

        batchDefinitionRepository.findById(batchId).ifPresent(batchDefinition -> {
            log.setBatchName(batchDefinition.getBatchName());
        });

        threadLogRepository.save(log);
    }

    public void logThreadEnd(UUID batchId) {
        ThreadLog log = threadLogRepository.findFirstByBatchIdOrderByStartTimeDesc(batchId);
        if (log != null) {
            log.setEndTime(LocalDateTime.now());
            threadLogRepository.save(log);
        }
    }

    public List<ThreadLog> getLogsByBatchId(UUID batchId) {
        return threadLogRepository.findByBatchId(batchId);
    }

    public List<ThreadLog> getAllThreadLogs() {
        return threadLogRepository.findAll();
    }
}

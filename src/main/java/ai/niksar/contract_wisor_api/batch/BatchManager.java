package ai.niksar.contract_wisor_api.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.model.BatchDefinition;
import ai.niksar.contract_wisor_api.repository.BatchDefinitionRepository;
import ai.niksar.contract_wisor_api.service.BatchLogService;
import ai.niksar.contract_wisor_api.util.Constants;

import java.util.List;
import java.util.UUID;

@Component
public class BatchManager {

    Logger logger = LoggerFactory.getLogger(BatchManager.class);

    @Autowired
    private BatchDefinitionRepository batchDefinitionRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private BatchLogService batchLogService;

    @Autowired
    private CronTaskScheduler cronTaskScheduler;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Value("${batch.timeout}")
    private Integer timeout;

    public void initializeBatches() {
        cronTaskScheduler.cancelAllScheduledBatches();
        cronTaskScheduler.shutdownExecutorService();
        taskScheduler.shutdown();

        List<BatchDefinition> activeBatches = batchDefinitionRepository.findByStatus(Constants.Status.ACTIVE);
        if (activeBatches.isEmpty()) {
            logger.info("Active batch size is zero.");
            return;
        }

        long validBatchCount = activeBatches.stream()
                .filter(batch -> isBatchServiceAvailable(batch.getServiceName()))
                .count();

        if (validBatchCount == 0) {
            logger.info("No valid batch services found.");
            return;
        }

        taskScheduler.setPoolSize((int) validBatchCount);
        taskScheduler.initialize();

        for (BatchDefinition batch : activeBatches) {
            String cronExpression = batch.getCronTime();
            String serviceName = batch.getServiceName();

            if (!isBatchServiceAvailable(serviceName)) {
                batchLogService.logBatchFailure(batch.getId(), "Service not available for batch: " + serviceName);
                logger.warn("Batch service not found: {}. Skipping batch ID: {}. Skipping batch name: ", serviceName, batch.getId(),batch.getBatchName());
                continue;
            }

            try {
                String basePackage = Constants.BATCH_BASE_PATH;
                String fullClassName = basePackage + serviceName;
                BatchService batchService = (BatchService) applicationContext.getBean(Class.forName(fullClassName));
                UUID batchId = batch.getId();

                cronTaskScheduler.schedule(cronExpression, batchService, batchId, batchLogService, batch.getMaxRetryCount(), batch.getRetryInterval(), timeout);

            } catch (Exception e) {
                batchLogService.logBatchFailure(batch.getId(), "Unexpected error: " + e.getMessage());
                logger.error("Unexpected error while scheduling batch: {}. Error: {}", batch.getId(), e.getMessage());
            }
        }
    }

    public boolean isBatchServiceAvailable(String serviceName) {
        try {
            String basePackage = Constants.BATCH_BASE_PATH;
            String fullClassName = basePackage + serviceName;
            applicationContext.getBean(Class.forName(fullClassName));
            return true;
        } catch (ClassNotFoundException | BeansException e) {
            return false;
        }
    }

}

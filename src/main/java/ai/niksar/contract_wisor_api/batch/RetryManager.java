package ai.niksar.contract_wisor_api.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.model.BatchLog;
import ai.niksar.contract_wisor_api.service.BatchLogService;

import java.util.UUID;
import java.util.concurrent.Callable;

@Component
public class RetryManager {

    private static final Logger logger = LoggerFactory.getLogger(RetryManager.class);

    public void runWithRetry(Callable<Void> batchTask, UUID batchId, BatchLogService batchLogService, int maxRetryCount, int retryInterval) {
        int retryCount = 0;
        boolean success = false;

        logger.info("Starting batch with retry mechanism. Batch ID: {}, Max Retry Count: {}", batchId, maxRetryCount);

        while (retryCount < maxRetryCount && !success) {
            BatchLog batchLog = batchLogService.logBatchStart(batchId);
            try {
                batchTask.call();
                batchLogService.logBatchSuccess(batchId);
                logger.info("Batch with ID: {} succeeded on attempt {}", batchId, retryCount + 1);
                success = true;
            } catch (Exception e) {
                retryCount++;
                logger.error("Batch with ID: {} failed on attempt {}. Error: {}", batchId, retryCount, e.getMessage());
                batchLogService.logBatchFailure(batchId, e.getMessage());
                if (retryCount < maxRetryCount) {
                    waitForRetry(retryInterval, retryCount);
                }
            }
        }

        if (!success) {
            logger.error("Batch with ID: {} failed after {} attempts", batchId, retryCount);
        }
    }

    private void waitForRetry(int retryInterval, int retryCount) {
        try {
            logger.info("Waiting for {} seconds before retrying. Retry count: {}", retryInterval * retryCount, retryCount);
            Thread.sleep(retryInterval * 1000L * retryCount);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.error("Retry interrupted. Error: {}", ex.getMessage());
        }
    }
}

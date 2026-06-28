package ai.niksar.contract_wisor_api.batch;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.exception.BatchTimeoutException;
import ai.niksar.contract_wisor_api.service.BatchLogService;

import java.util.UUID;
import java.util.concurrent.*;

@Component
public class BatchExecutor {

    private static final Logger logger = LoggerFactory.getLogger(BatchExecutor.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @Autowired
    private LockManager lockManager;

    @Autowired
    private RetryManager retryManager;

    @Autowired
    private MeterRegistry meterRegistry;

    public void executeBatch(BatchService batchService, UUID batchId, BatchLogService batchLogService, int maxRetryCount, int retryInterval, int timeout) {
        Timer.Sample sample = Timer.start(meterRegistry);

        logger.info("Starting execution of batch with ID: {} on Thread: {}", batchId, Thread.currentThread().getName());

        if (lockManager.acquireLock(batchId)) {
            try {
                retryManager.runWithRetry(() -> {
                    try {
                        runBatchWithTimeout(batchService, batchId, timeout);
                        logger.info("Batch with ID: {} executed successfully", batchId);
                        return null;
                    } catch (Exception e) {
                        logger.error("Error occurred during batch execution with ID: {}. Error: {}", batchId, e.getMessage());
                        throw new RuntimeException(e);
                    }
                }, batchId, batchLogService, maxRetryCount, retryInterval);
            } finally {
                lockManager.releaseLock(batchId);
                logger.info("Lock released for batch ID: {}", batchId);
            }
        } else {
            logger.warn("Could not acquire lock for batch ID: {}", batchId);
        }

        logger.info("Execution of batch with ID: {} completed on Thread: {}", batchId, Thread.currentThread().getName());

        sample.stop(Timer.builder("batch.execution.time")
                .publishPercentiles(0.5, 0.95)
                .publishPercentileHistogram()
                .register(meterRegistry));
    }

    private void runBatchWithTimeout(BatchService batchService, UUID batchId, int timeout) throws Exception {
        logger.info("Running batch with timeout. Batch ID: {}, Timeout: {} seconds", batchId, timeout);

        Future<?> future = executorService.submit(batchService::execute);

        try {
            future.get(timeout, TimeUnit.SECONDS);
            logger.info("Batch with ID: {} completed within the timeout", batchId);
        } catch (TimeoutException e) {
            future.cancel(true);
            logger.error("Batch with ID: {} timed out after {} seconds", batchId, timeout);
            throw new BatchTimeoutException("Batch with ID: " + batchId + " timed out after " + timeout + " seconds");
        }
    }
}

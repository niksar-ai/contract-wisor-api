package ai.niksar.contract_wisor_api.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;
import ai.niksar.contract_wisor_api.service.BatchLogService;

import java.util.UUID;
import java.util.concurrent.*;

@Component
public class CronTaskScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CronTaskScheduler.class);
    private final ConcurrentHashMap<UUID, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // ExecutorService

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private BatchExecutor batchExecutor;

    // The taskScheduler bean is fully configured and initialized in SchedulerConfig.
    // It must NOT be re-initialized here (doing so terminated the live executor and
    // broke @Async + scheduled batches).

    public void schedule(String cronExpression, BatchService batchService, UUID batchId, BatchLogService batchLogService, int maxRetryCount, int retryInterval, int timeout) {
        CronTrigger trigger = new CronTrigger(cronExpression);
        logger.info("Scheduling batch with ID: {} and Cron Expression: {}", batchId, cronExpression);

        if (scheduledTasks.containsKey(batchId)) {
            cancelScheduledBatch(batchId);
        }

        ScheduledFuture<?> future = taskScheduler.schedule(() -> batchExecutor.executeBatch(batchService, batchId, batchLogService, maxRetryCount, retryInterval, timeout), trigger);
        scheduledTasks.put(batchId, future);
    }

    public void cancelScheduledBatch(UUID batchId) {
        ScheduledFuture<?> future = scheduledTasks.remove(batchId);
        if (future != null) {
            future.cancel(true);
            logger.info("Scheduled batch with ID: {} has been cancelled.", batchId);
        }
    }
    public void cancelAllScheduledBatches() {
        for (UUID batchId : scheduledTasks.keySet()) {
            cancelScheduledBatch(batchId);
        }
    }


    public void shutdownExecutorService() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.error("Executor service did not terminate.");
                }
            }
        } catch (InterruptedException ie) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void gracefulShutdown() {
        logger.info("Shutting down ExecutorService gracefully...");
        shutdownExecutorService();
        logger.info("ExecutorService shutdown complete.");
    }
}

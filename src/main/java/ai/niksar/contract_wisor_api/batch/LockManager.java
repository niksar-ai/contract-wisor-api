package ai.niksar.contract_wisor_api.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockManager {

    private static final Logger logger = LoggerFactory.getLogger(LockManager.class);
    private final ConcurrentHashMap<UUID, Lock> batchLocks = new ConcurrentHashMap<>();

    public boolean acquireLock(UUID batchId) {
        Lock lock = batchLocks.computeIfAbsent(batchId, k -> new ReentrantLock());
        try {
            if (!lock.tryLock(5, TimeUnit.SECONDS)) {
                logger.warn("Batch with ID: {} lock acquisition timed out.", batchId);
                return false;
            }
            logger.info("Lock acquired for batch with ID: {}", batchId);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.error("Lock acquisition interrupted for batch ID: {}", batchId);
            return false;
        }
    }

    public void releaseLock(UUID batchId) {
        Lock lock = batchLocks.get(batchId);
        if (lock != null) {
            lock.unlock();
            batchLocks.remove(batchId);
            logger.info("Lock released for batch ID: {}", batchId);
        } else {
            logger.warn("No lock found to release for batch ID: {}", batchId);
        }
    }
}

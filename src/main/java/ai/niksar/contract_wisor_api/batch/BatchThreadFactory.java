package ai.niksar.contract_wisor_api.batch;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class BatchThreadFactory implements ThreadFactory {

    private final AtomicInteger threadId = new AtomicInteger(1);
    private final String poolName;

    public BatchThreadFactory(String poolName) {
        this.poolName = poolName;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(poolName + "-Thread-" + threadId.getAndIncrement());
        return thread;
    }
}
package ai.niksar.contract_wisor_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import ai.niksar.contract_wisor_api.batch.BatchThreadFactory;

@Configuration
public class SchedulerConfig {

    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        // Configure the scheduler fully here and initialize it exactly once.
        // (Previously CronTaskScheduler called initialize() a second time, which
        // orphaned the live executor and left @Async/scheduling rejecting tasks.)
        scheduler.setThreadFactory(new BatchThreadFactory("BatchPool"));
        scheduler.initialize();
        return scheduler;
    }
}

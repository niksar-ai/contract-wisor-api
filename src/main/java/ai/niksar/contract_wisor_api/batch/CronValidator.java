package ai.niksar.contract_wisor_api.batch;

import org.springframework.scheduling.support.CronTrigger;
import ai.niksar.contract_wisor_api.exception.CriticalBatchException;

public class CronValidator {

    public static boolean isValidCronExpression(String cronExpression) {
        try {
            new CronTrigger(cronExpression);
            return true;
        } catch (IllegalArgumentException e) {
            throw new CriticalBatchException("The cron expression '" + cronExpression + "' is invalid. Please check the format and try again.");
        }
    }
}
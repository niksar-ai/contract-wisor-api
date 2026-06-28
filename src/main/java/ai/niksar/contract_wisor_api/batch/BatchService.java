package ai.niksar.contract_wisor_api.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ai.niksar.contract_wisor_api.exception.CriticalBatchException;

public interface BatchService {
    void execute() throws CriticalBatchException;
    Logger contractWisorLogger = LoggerFactory.getLogger(BatchService.class);
}
package ai.niksar.contract_wisor_api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ai.niksar.contract_wisor_api.batch.BatchManager;
import ai.niksar.contract_wisor_api.service.RoleMenuService;

@Component
public class Initializer implements CommandLineRunner {

    Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private BatchManager batchManager;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Starting Initializer.");
            initializeBatchesIfEnabled(args);
            logger.info("Initializer started.");
        } catch (Exception e) {
            logger.error("Initializer cannot be started. ", e);
        }
    }

    private void initializeBatchesIfEnabled(String... args) {
        if (checkIfBatchEnabled(args)) {
            batchManager.initializeBatches();
            logger.info("Batch jobs have been enabled and initialized.");
        } else {
            logger.info("Batch jobs are disabled as per configuration.");
        }
    }

    private boolean checkIfBatchEnabled(String... args) {
        for (String arg : args) {
            if (arg.startsWith("--scheduler")) {
                String[] keyValue = arg.split("=");
                if (keyValue.length == 2 && keyValue[1].equalsIgnoreCase("true")) {
                    return true;
                }
                break;
            }
        }
        return false;
    }
}

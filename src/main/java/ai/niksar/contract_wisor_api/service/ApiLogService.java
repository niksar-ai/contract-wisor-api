package ai.niksar.contract_wisor_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ai.niksar.contract_wisor_api.model.ApiLog;
import ai.niksar.contract_wisor_api.repository.ApiLogRepository;

@Service
public class ApiLogService {

    Logger logger = LoggerFactory.getLogger(ApiLogService.class);

    private final ApiLogRepository apiLogRepository;

    @Autowired
    public ApiLogService(ApiLogRepository apiLogRepository) {
        this.apiLogRepository = apiLogRepository;
    }
    @Async
    public void saveLog(ApiLog apiLog) {
        apiLogRepository.save(apiLog);
    }

}

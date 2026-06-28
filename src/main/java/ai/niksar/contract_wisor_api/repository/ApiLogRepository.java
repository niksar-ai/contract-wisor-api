package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ai.niksar.contract_wisor_api.model.ApiLog;

public interface ApiLogRepository extends JpaRepository<ApiLog, Long>, JpaSpecificationExecutor<ApiLog> {
}

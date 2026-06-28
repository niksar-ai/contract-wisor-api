package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ai.niksar.contract_wisor_api.model.BatchLog;

import java.util.Optional;
import java.util.UUID;

public interface BatchLogRepository extends JpaRepository<BatchLog, UUID> {
    Optional<BatchLog> findByBatchId(UUID batchId);
    Optional<BatchLog> findTopByBatchIdOrderByStartTimeDesc(UUID batchId);
}

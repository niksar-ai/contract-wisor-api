package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.ThreadLog;

import java.util.List;
import java.util.UUID;

@Repository
public interface ThreadLogRepository extends JpaRepository<ThreadLog, Long> {

    List<ThreadLog> findByBatchId(UUID batchId);

    ThreadLog findFirstByBatchIdOrderByStartTimeDesc(UUID batchId);
}
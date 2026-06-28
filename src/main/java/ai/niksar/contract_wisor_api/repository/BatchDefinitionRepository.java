package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ai.niksar.contract_wisor_api.model.BatchDefinition;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BatchDefinitionRepository extends JpaRepository<BatchDefinition, UUID> {
    List<BatchDefinition> findByStatus(String status);
    Optional<BatchDefinition> findByServiceName(String serviceName);
    Optional<BatchDefinition> findById(UUID batchId);

}
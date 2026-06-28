package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.ReferenceData;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReferenceDataRepository extends JpaRepository<ReferenceData, UUID> {
    List<ReferenceData> findByRefId(String refId);
}

package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.DocumentDetail;

import java.util.UUID;

@Repository
public interface DocumentDetailRepository extends JpaRepository<DocumentDetail, UUID> {}

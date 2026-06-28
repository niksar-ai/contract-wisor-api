package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.DocumentView;

import java.util.UUID;

@Repository
public interface DocumentViewRepository extends JpaRepository<DocumentView, UUID> {}

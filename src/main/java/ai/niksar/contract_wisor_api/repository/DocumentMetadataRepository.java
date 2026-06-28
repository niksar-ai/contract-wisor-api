package ai.niksar.contract_wisor_api.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.DocumentMetadata;

import java.util.UUID;

@Repository
public interface DocumentMetadataRepository extends JpaRepository<DocumentMetadata, UUID> {
    DocumentMetadata findByDocumentId(UUID documentId);
    @Modifying
    @Transactional
    @Query("DELETE FROM DocumentMetadata dm WHERE dm.documentId = :documentId")
    void deleteByDocumentId(UUID documentId);}

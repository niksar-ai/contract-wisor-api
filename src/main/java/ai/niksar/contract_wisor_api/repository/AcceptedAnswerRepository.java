package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.model.DocumentAcceptedAnswer;

import java.util.List;
import java.util.UUID;

@Repository
public interface AcceptedAnswerRepository extends JpaRepository<DocumentAcceptedAnswer, UUID> {
    @Query("SELECT a.id FROM DocumentAcceptedAnswer a WHERE a.documentId = :documentId AND a.code = :code ")
    UUID existAcceptedAnswerId(@PathVariable UUID documentId, @PathVariable String code);
    @Query("SELECT a FROM DocumentAcceptedAnswer a WHERE a.documentId = :documentId ")
    List<DocumentAcceptedAnswer> getAcceptedAnswerList(@PathVariable UUID documentId);
}
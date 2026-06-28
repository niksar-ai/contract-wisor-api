package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.model.DocumentComment;

import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentCommentRepository extends JpaRepository<DocumentComment, UUID> {
    @Query("SELECT d FROM DocumentComment d WHERE d.documentId = :documentId")
    List<DocumentComment> findByDocumentId(@PathVariable UUID documentId);
}

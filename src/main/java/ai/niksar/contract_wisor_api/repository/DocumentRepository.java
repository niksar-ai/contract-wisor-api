package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ai.niksar.contract_wisor_api.model.Document;

import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> , JpaSpecificationExecutor<Document> {
    @Query("SELECT COUNT(d) FROM Document d WHERE d.metadataId IS NOT NULL")
    Long findDocumentsWithMetadataIdNotNull();

    @Query("SELECT SUM(d.size) FROM Document d")
    BigInteger sumSizeOfDocuments();

    @Query("SELECT COUNT(d) FROM Document d WHERE EXISTS (SELECT a FROM Analyze a WHERE a.documentId = d.id)")
    Long countAnalyzeDocuments(Specification<Document> spec);

    @Query("SELECT d FROM Document d " +
            "JOIN DocumentView dv ON d.id = dv.viewDocumentId " +
            "JOIN DocumentMetadata dm ON d.metadataId = dm.id " +
            "JOIN DocumentType dt ON d.documentTypeId = dt.id " +
            "WHERE dv.viewUser = :user " +
            "AND dv.viewTime = (SELECT MAX(subDv.viewTime) FROM DocumentView subDv WHERE subDv.viewDocumentId = d.id AND subDv.viewUser = :user) " +
            "ORDER BY dv.viewDate DESC, dv.viewTime DESC")
    Page<Document> sortedDocumentsView(@Param("user") String user, Pageable pageable);

    @Query("SELECT dr.child FROM DocumentRelation dr WHERE dr.parentId = :parentId")
    List<Document> findRelatedDocuments(@Param("parentId") UUID parentId);

    @Modifying
    @Query("UPDATE Document d SET d.state = :state, d.updateDate = :updateDate, d.updateTime = :updateTime WHERE d.id = :documentId")
    void updateDocumentState(@Param("documentId") UUID documentId,
                             @Param("state") String state,
                             @Param("updateDate") String updateDate,
                             @Param("updateTime") String updateTime);

    @Query("SELECT state, COUNT(d) FROM Document d GROUP BY state")
    List<Object[]> getCountGroupByWithState();

    @Query("SELECT d FROM Document d WHERE d.parseState = '1' OR d.analyzeState = '1' ")
    List<Document> progressDocumentList();
}



package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.model.AnalyzeDetail;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyzeDetailRepository extends JpaRepository<AnalyzeDetail, UUID> {
    @Query("SELECT a FROM AnalyzeDetail a WHERE a.status = '1' AND a.state = '1'")
    List<AnalyzeDetail> findActiveList();
    @Query("SELECT a FROM AnalyzeDetail a WHERE a.status = '1' AND a.state = '1' AND a.documentId = :documentId ")
    List<AnalyzeDetail> findActiveListByDocumentId(@PathVariable UUID documentId);

}
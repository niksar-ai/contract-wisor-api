package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.dto.AnalyzeDTO;
import ai.niksar.contract_wisor_api.model.Analyze;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyzeRepository extends JpaRepository<Analyze, UUID> , JpaSpecificationExecutor<Analyze> {
    @Query("SELECT a FROM Analyze a WHERE a.status = '1' AND a.state = '1' AND a.documentId = :documentId ")
    List<Analyze> findActiveListByDocumentId(@PathVariable UUID documentId);
    @Query("SELECT a FROM Analyze a WHERE a.documentId = :documentId ")
    List<Analyze> findAnalyzeListByDocumentId(@PathVariable UUID documentId);
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.AnalyzeDTO(" +
            "a.id, a.createdAt, a.updatedAt, a.processStarted, a.processFinished, " +
            "a.state, a.status, a.createUser, a.updateUser, " +
            "asr.successRate, asr.totalTokens , asr.promptTokens , asr.completionTokens , asr.successfulRequests , asr.totalCost ) " +
            "FROM Analyze a " +
            "LEFT JOIN AnalyzeStats asr ON a.id = asr.analyzeId AND asr.version = :version " +
            "WHERE a.documentId = :documentId AND a.status = '1' ")
    List<AnalyzeDTO> findAnalyzeDTOById(@Param("documentId") UUID documentId, @Param("version") String version);
}
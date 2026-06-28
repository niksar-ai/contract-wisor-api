package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.dto.AnalyzeAnswerDTO;
import ai.niksar.contract_wisor_api.model.AnalyzeAnswer;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyzeAnswerRepository extends JpaRepository<AnalyzeAnswer, UUID> {
    @Query("SELECT a FROM AnalyzeAnswer a WHERE a.analyzeId = :analyzeId AND a.version = :version ")
    List<AnalyzeAnswer> findByAnalyzeIdWithVersion(@PathVariable UUID analyzeId, @PathVariable String version);
    @Query("SELECT new ai.niksar.contract_wisor_api.dto.AnalyzeAnswerDTO(a.shortAnswerType, a.longAnswer, a.summaryAnswer, a.shortAnswerValue, a.shortAnswerGenerated , a.successRate ) " +
            "FROM AnalyzeAnswer a WHERE a.analyzeId = :analyzeId AND a.questionCode = :code AND a.version = :version")
    AnalyzeAnswerDTO findByAnalyzeIdAndCode(@Param("analyzeId") UUID analyzeId, @Param("code") String code,@Param("version") String version );
    @Modifying
    @Query("DELETE FROM AnalyzeAnswer a WHERE a.analyzeId = :analyzeId")
    void deleteByAnalyzeId(@Param("analyzeId") UUID analyzeId);
}
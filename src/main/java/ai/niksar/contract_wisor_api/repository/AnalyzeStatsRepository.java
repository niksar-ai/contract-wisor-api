package ai.niksar.contract_wisor_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;
import ai.niksar.contract_wisor_api.model.AnalyzeStats;

import java.util.UUID;

@Repository
public interface AnalyzeStatsRepository extends JpaRepository<AnalyzeStats, UUID> {
    @Query("SELECT a FROM AnalyzeStats a WHERE a.analyzeId = :analyzeId AND a.version = :version ")
    AnalyzeStats findByAnalyzeId(@PathVariable("analyzeId") UUID analyzeId, @PathVariable("version") String version);
}
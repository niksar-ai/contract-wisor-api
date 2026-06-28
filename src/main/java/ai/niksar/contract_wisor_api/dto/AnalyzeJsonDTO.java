package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class AnalyzeJsonDTO {
    @JsonProperty("document_name")
    private String documentName;

    @JsonProperty("queries")
    private List<AnalyzeQueryDTO> queries;

    @JsonProperty("stats")
    private Map<String, Object> stats;

    @JsonProperty("version_stats")
    private List<Map<String, Object>> versionStats;

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public List<AnalyzeQueryDTO> getQueries() {
        return queries;
    }

    public void setQueries(List<AnalyzeQueryDTO> queries) {
        this.queries = queries;
    }

    public Map<String, Object> getStats() {
        return stats;
    }

    public void setStats(Map<String, Object> stats) {
        this.stats = stats;
    }

    public List<Map<String, Object>> getVersionStats() {
        return versionStats;
    }

    public void setVersionStats(List<Map<String, Object>> versionStats) {
        this.versionStats = versionStats;
    }
}

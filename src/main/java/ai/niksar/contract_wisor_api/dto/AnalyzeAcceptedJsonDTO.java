package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

public class AnalyzeAcceptedJsonDTO {
    private UUID documentId;

    @JsonProperty("queries")
    private List<AnalyzeAcceptedQueryDTO> queries;


    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public List<AnalyzeAcceptedQueryDTO> getQueries() {
        return queries;
    }

    public void setQueries(List<AnalyzeAcceptedQueryDTO> queries) {
        this.queries = queries;
    }

}

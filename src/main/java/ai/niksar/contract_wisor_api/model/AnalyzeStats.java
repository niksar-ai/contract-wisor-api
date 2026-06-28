package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "analyze_stats")
public class AnalyzeStats {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "analyze_id")
    private UUID analyzeId;

    @Column(name ="version")
    private String version;

    @Column(name = "tags",columnDefinition = "TEXT")
    private String tagsJson;

    @Column(name = "meta",columnDefinition = "TEXT")
    private String metaJson;

    @Column(name = "success_rate", columnDefinition = "Decimal")
    private BigDecimal successRate;

    @Column(name = "lasted", columnDefinition = "Decimal")
    private BigDecimal lasted;

    @Column(name = "total_tokens", columnDefinition = "Integer")
    private Integer totalTokens;

    @Column(name = "prompt_tokens", columnDefinition = "Integer")
    private Integer promptTokens;

    @Column(name = "completion_tokens", columnDefinition = "Integer")
    private Integer completionTokens;

    @Column(name = "successful_requests", columnDefinition = "Integer")
    private Integer successfulRequests;

    @Column(name = "total_cost", columnDefinition = "Decimal")
    private BigDecimal totalCost;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getAnalyzeId() {
        return analyzeId;
    }

    public void setAnalyzeId(UUID analyzeId) {
        this.analyzeId = analyzeId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTagsJson() {
        return tagsJson;
    }

    public void setTagsJson(String tagsJson) {
        this.tagsJson = tagsJson;
    }

    public String getMetaJson() {
        return metaJson;
    }

    public void setMetaJson(String metaJson) {
        this.metaJson = metaJson;
    }


    public BigDecimal getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }

    public BigDecimal getLasted() {
        return lasted;
    }

    public void setLasted(BigDecimal lasted) {
        this.lasted = lasted;
    }

    public Integer getTotalTokens() {
        return totalTokens;
    }

    public void setTotalTokens(Integer totalTokens) {
        this.totalTokens = totalTokens;
    }

    public Integer getPromptTokens() {
        return promptTokens;
    }

    public void setPromptTokens(Integer promptTokens) {
        this.promptTokens = promptTokens;
    }

    public Integer getCompletionTokens() {
        return completionTokens;
    }

    public void setCompletionTokens(Integer completionTokens) {
        this.completionTokens = completionTokens;
    }

    public Integer getSuccessfulRequests() {
        return successfulRequests;
    }

    public void setSuccessfulRequests(Integer successfulRequests) {
        this.successfulRequests = successfulRequests;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }
}

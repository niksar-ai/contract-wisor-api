package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.dto.AnalyzeShortAnswerDTO;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "analyze_answers")
public class AnalyzeAnswer {

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

    @Column(name = "short_answer",columnDefinition = "TEXT")
    private String shortAnswerJson;

    @Transient
    private AnalyzeShortAnswerDTO shortAnswer;

    @Column(name= "short_answer_type")
    private String shortAnswerType;

    @Column(name= "short_answer_generated")
    private boolean shortAnswerGenerated;

    @Column(name= "short_answer_value" ,columnDefinition = "TEXT")
    private String shortAnswerValue;

    @Column(name = "long_answer", columnDefinition = "TEXT")
    private String longAnswer;

    @Column(name = "summary_answer", columnDefinition = "TEXT")
    private String summaryAnswer;

    @Column(name = "debug",columnDefinition = "TEXT")
    private String debugJson;

    @Column(name= "question_code")
    private String questionCode;

    @Column(name = "success_rate", columnDefinition = "Decimal")
    private BigDecimal successRate;

    @Column(name = "lasted", columnDefinition = "Decimal")
    private BigDecimal lasted;

    // Getters and Setters
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

    public String getShortAnswerJson() {
        return shortAnswerJson;
    }

    public void setShortAnswerJson(String shortAnswerJson) {
        this.shortAnswerJson = shortAnswerJson;
    }

    public AnalyzeShortAnswerDTO getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(AnalyzeShortAnswerDTO shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public String getLongAnswer() {
        return longAnswer;
    }

    public void setLongAnswer(String longAnswer) {
        this.longAnswer = longAnswer;
    }

    public String getSummaryAnswer() {
        return summaryAnswer;
    }

    public void setSummaryAnswer(String summaryAnswer) {
        this.summaryAnswer = summaryAnswer;
    }

    public String getDebugJson() {
        return debugJson;
    }

    public void setDebugJson(String debugJson) {
        this.debugJson = debugJson;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    @PostLoad
    private void postLoad() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        if (shortAnswerJson != null) {
            this.shortAnswer = mapper.readValue(shortAnswerJson, AnalyzeShortAnswerDTO.class);
        }
    }

    public String getShortAnswerType() {
        return shortAnswerType;
    }

    public void setShortAnswerType(String shortAnswerType) {
        this.shortAnswerType = shortAnswerType;
    }


    public boolean isShortAnswerGenerated() {
        return shortAnswerGenerated;
    }

    public void setShortAnswerGenerated(boolean shortAnswerGenerated) {
        this.shortAnswerGenerated = shortAnswerGenerated;
    }

    public String getShortAnswerValue() {
        return shortAnswerValue;
    }

    public void setShortAnswerValue(String shortAnswerValue) {
        this.shortAnswerValue = shortAnswerValue;
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
}

package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.dto.AnalyzeShortAnswerDTO;

import java.util.UUID;

@Entity
@Table(name = "analyze_accepted_answers")
public class DocumentAcceptedAnswer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "document_id")
    private UUID documentId;

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

    @Column(name= "code")
    private String code;



    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}

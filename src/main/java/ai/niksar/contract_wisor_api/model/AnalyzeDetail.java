package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.dto.AnalyzeQuestionDTO;
import ai.niksar.contract_wisor_api.util.Updatable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "analyze")
public class AnalyzeDetail {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="document_id")
    private UUID documentId;

    @Column(name="question_id_list")
    @JsonIgnore
    private List<UUID> questionIdList;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "process_started")
    private LocalDateTime processStarted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @Column(name = "process_finished")
    private LocalDateTime processFinished;

    @Column(name= "state")
    private String state;

    @Column(name= "status")
    private String status;

    @Lob
    @Column(name = "result")
    @JsonIgnore
    private String result;

    @Column(name = "create_user")
    @JsonIgnore
    private String createUsername;

    @Column(name = "update_user")
    @JsonIgnore
    private String updateUsername;

    @Column(name = "success_rate", columnDefinition = "Decimal")
    private BigDecimal successRate;

    @Column(name = "lasted", columnDefinition = "Decimal")
    @JsonIgnore
    private BigDecimal lasted;

    @Column(name = "top_version")
    @JsonIgnore
    private String topVersion;
    @Transient
    private List<AnalyzeQuestionDTO> questions;

    @Transient
    private Document document;
    @Transient
    private User createUser;
    @Transient
    private User updateUser;
    @Transient
    private Integer totalTokens;
    @Transient
    private Integer promptTokens;
    @Transient
    private Integer completionTokens;
    @Transient
    private Integer successfulRequests;
    @Transient
    private BigDecimal totalCost;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt=  LocalDateTime.now();
        this.state  = "0";
        this.status = "0";
    }
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public List<UUID> getQuestionIdList() {
        return questionIdList;
    }

    public void setQuestionIdList(List<UUID> questionIdList) {
        this.questionIdList = questionIdList;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalDateTime getProcessFinished() {
        return processFinished;
    }

    public void setProcessFinished(LocalDateTime processFinished) {
        this.processFinished = processFinished;
    }

    public LocalDateTime getProcessStarted() {
        return processStarted;
    }

    public void setProcessStarted(LocalDateTime processStarted) {
        this.processStarted = processStarted;
    }

    public List<AnalyzeQuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<AnalyzeQuestionDTO> questions) {
        this.questions = questions;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }



    public String getCreateUsername() {
        return createUsername;
    }

    public void setCreateUsername(String createUsername) {
        this.createUsername = createUsername;
    }

    public String getUpdateUsername() {
        return updateUsername;
    }

    public void setUpdateUsername(String updateUsername) {
        this.updateUsername = updateUsername;
    }

    public User getCreateUser() {
        return createUser;
    }

    public void setCreateUser(User createUser) {
        this.createUser = createUser;
    }

    public User getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(User updateUser) {
        this.updateUser = updateUser;
    }



    public String getTopVersion() {
        return topVersion;
    }

    public void setTopVersion(String topVersion) {
        this.topVersion = topVersion;
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

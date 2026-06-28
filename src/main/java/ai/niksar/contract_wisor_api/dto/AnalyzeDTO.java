package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class AnalyzeDTO {

    private UUID id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processStarted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime processFinished;
    private String state;
    private String status;
    private String createUser;
    private String updateUser;
    private BigDecimal successRate;
    private Integer totalTokens;
    private Integer promptTokens;
    private Integer completionTokens;
    private Integer successfulRequests;
    private BigDecimal totalCost;

    public AnalyzeDTO(UUID id,LocalDateTime createdAt,LocalDateTime updatedAt,LocalDateTime processStarted,LocalDateTime processFinished,String state,String status,String createUser,String updateUser,BigDecimal successRate,Integer totalTokens,Integer promptTokens,Integer completionTokens, Integer successfulRequests, BigDecimal totalCost ){
        this.id=id;
        this.createdAt=createdAt;
        this.updatedAt=updatedAt;
        this.processStarted=processStarted;
        this.processFinished=processFinished;
        this.state=state;
        this.status=status;
        this.createUser=createUser;
        this.updateUser=updateUser;
        this.successRate=successRate;
        this.totalTokens=totalTokens;
        this.promptTokens=promptTokens;
        this.completionTokens=completionTokens;
        this.successfulRequests=successfulRequests;
        this.totalCost=totalCost;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public LocalDateTime getProcessStarted() {
        return processStarted;
    }

    public void setProcessStarted(LocalDateTime processStarted) {
        this.processStarted = processStarted;
    }

    public LocalDateTime getProcessFinished() {
        return processFinished;
    }

    public void setProcessFinished(LocalDateTime processFinished) {
        this.processFinished = processFinished;
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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public BigDecimal getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
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

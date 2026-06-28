package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Updatable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "analyze")
public class Analyze {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name ="document_id",updatable = false,insertable = false)
    @JsonIgnore
    private UUID documentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_id")
    private Document document;
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

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;

    @Column(name = "success_rate", columnDefinition = "Decimal")
    private BigDecimal successRate;

    @Column(name = "lasted", columnDefinition = "Decimal")
    private BigDecimal lasted;

    @Column(name = "top_version")
    private String topVersion;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
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

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
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
}

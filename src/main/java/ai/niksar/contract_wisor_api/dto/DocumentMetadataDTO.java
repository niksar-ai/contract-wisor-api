package ai.niksar.contract_wisor_api.dto;

import jakarta.persistence.Column;

import java.util.UUID;

public class DocumentMetadataDTO {
    @Column(name = "id")
    private UUID id;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "document_id", nullable = false)
    private UUID documentId;
    @Column(name = "create_date")
    private String createDate;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "company")
    private String company;

    @Column(name = "expired_contract")
    private Boolean expiredContract;

    @Column(name = "expiry_date")
    private String expiryDate;

    @Column(name = "description")
    private String description;

    @Column(name = "amended")
    private Boolean amended;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Boolean isExpiredContract() {
        return expiredContract;
    }

    public void setExpiredContract(Boolean expiredContract) {
        this.expiredContract = expiredContract;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isAmended() {
        return amended;
    }

    public void setAmended(Boolean amended) {
        this.amended = amended;
    }
}

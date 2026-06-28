package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class DocumentMetadataFilterDTO {

    private String status;
    private UUID documentId;
    private String createDate;
    private String createTime;
    private String company;
    private String expiredContract;
    private String relationType;
    private String expiryDateFirst;
    private String description;
    private String amended;
    private String expiryDateLast;

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

    public String getExpiredContract() {
        return expiredContract;
    }

    public void setExpiredContract(String expiredContract) {
        this.expiredContract = expiredContract;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAmended() {
        return amended;
    }

    public void setAmended(String amended) {
        this.amended = amended;
    }

    public String getExpiryDateFirst() {
        return expiryDateFirst;
    }

    public void setExpiryDateFirst(String expiryDateFirst) {
        this.expiryDateFirst = expiryDateFirst;
    }

    public String getExpiryDateLast() {
        return expiryDateLast;
    }

    public void setExpiryDateLast(String expiryDateLast) {
        this.expiryDateLast = expiryDateLast;
    }
}


package ai.niksar.contract_wisor_api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class AnalyzeFilterDTO {
    private String state;
    private String status;
    private LocalDateTime createdAtStart;
    private LocalDateTime createdAtEnd;
    private LocalDateTime updatedAtStart;
    private LocalDateTime updatedAtEnd;
    private LocalDateTime processStartedStart;
    private LocalDateTime processStartedEnd;
    private LocalDateTime processFinishedStart;
    private LocalDateTime processFinishedEnd;
    private String createUser;
    private String updateUser;
    private UUID documentId;
    private UUID documentTypeId;
    private String documentName;
    private String isMetadataExist;

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

    public LocalDateTime getCreatedAtStart() {
        return createdAtStart;
    }

    public void setCreatedAtStart(LocalDateTime createdAtStart) {
        this.createdAtStart = createdAtStart;
    }

    public LocalDateTime getCreatedAtEnd() {
        return createdAtEnd;
    }

    public void setCreatedAtEnd(LocalDateTime createdAtEnd) {
        this.createdAtEnd = createdAtEnd;
    }

    public LocalDateTime getUpdatedAtStart() {
        return updatedAtStart;
    }

    public void setUpdatedAtStart(LocalDateTime updatedAtStart) {
        this.updatedAtStart = updatedAtStart;
    }

    public LocalDateTime getUpdatedAtEnd() {
        return updatedAtEnd;
    }

    public void setUpdatedAtEnd(LocalDateTime updatedAtEnd) {
        this.updatedAtEnd = updatedAtEnd;
    }

    public LocalDateTime getProcessStartedStart() {
        return processStartedStart;
    }

    public void setProcessStartedStart(LocalDateTime processStartedStart) {
        this.processStartedStart = processStartedStart;
    }

    public LocalDateTime getProcessStartedEnd() {
        return processStartedEnd;
    }

    public void setProcessStartedEnd(LocalDateTime processStartedEnd) {
        this.processStartedEnd = processStartedEnd;
    }

    public LocalDateTime getProcessFinishedStart() {
        return processFinishedStart;
    }

    public void setProcessFinishedStart(LocalDateTime processFinishedStart) {
        this.processFinishedStart = processFinishedStart;
    }

    public LocalDateTime getProcessFinishedEnd() {
        return processFinishedEnd;
    }

    public void setProcessFinishedEnd(LocalDateTime processFinishedEnd) {
        this.processFinishedEnd = processFinishedEnd;
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

    public UUID getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(UUID documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getIsMetadataExist() {
        return isMetadataExist;
    }

    public void setIsMetadataExist(String isMetadataExist) {
        this.isMetadataExist = isMetadataExist;
    }
}

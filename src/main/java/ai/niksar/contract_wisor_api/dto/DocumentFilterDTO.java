package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class DocumentFilterDTO {
    private String status;
    private String state;
    private String parseState;
    private String analyzeState;
    private String name;
    private String firstUpdateDate;
    private String lastUpdateDate;
    private String firstCreateDate;
    private String lastCreateDate;
    private Integer pageCountMin;
    private Integer pageCountMax;
    private Long sizeMin;
    private Long sizeMax;
    private String isMetadataExist;
    private String createUser;
    private String updateUser;
    private UUID documentTypeId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPageCountMin() {
        return pageCountMin;
    }

    public void setPageCountMin(Integer pageCountMin) {
        this.pageCountMin = pageCountMin;
    }

    public Integer getPageCountMax() {
        return pageCountMax;
    }

    public void setPageCountMax(Integer pageCountMax) {
        this.pageCountMax = pageCountMax;
    }

    public Long getSizeMin() {
        return sizeMin;
    }

    public void setSizeMin(Long sizeMin) {
        this.sizeMin = sizeMin;
    }

    public Long getSizeMax() {
        return sizeMax;
    }

    public void setSizeMax(Long sizeMax) {
        this.sizeMax = sizeMax;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFirstUpdateDate() {
        return firstUpdateDate;
    }

    public void setFirstUpdateDate(String firstUpdateDate) {
        this.firstUpdateDate = firstUpdateDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getFirstCreateDate() {
        return firstCreateDate;
    }

    public void setFirstCreateDate(String firstCreateDate) {
        this.firstCreateDate = firstCreateDate;
    }

    public String getLastCreateDate() {
        return lastCreateDate;
    }

    public void setLastCreateDate(String lastCreateDate) {
        this.lastCreateDate = lastCreateDate;
    }

    public String getIsMetadataExist() {
        return isMetadataExist;
    }

    public void setIsMetadataExist(String isMetadataExist) {
        this.isMetadataExist = isMetadataExist;
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

    public UUID getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(UUID documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getParseState() {
        return parseState;
    }

    public void setParseState(String parseState) {
        this.parseState = parseState;
    }

    public String getAnalyzeState() {
        return analyzeState;
    }

    public void setAnalyzeState(String analyzeState) {
        this.analyzeState = analyzeState;
    }
}

package ai.niksar.contract_wisor_api.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import ai.niksar.contract_wisor_api.model.DocumentMetadata;
import ai.niksar.contract_wisor_api.model.DocumentType;

import java.util.List;
import java.util.UUID;

public class DocumentAnalyzeDTO {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "page_count")
    private int pageCount;

    @Column(name = "size")
    private long size;

    @Column(name = "state")
    private String state;

    @Column(name = "create_date")
    private String createDate;

    @Column(name = "create_time")
    private String createTime;

    @Column(name = "update_date")
    private String updateDate;

    @Column(name = "update_time")
    private String updateTime;

    @Column(name = "status")
    private String status;


    @Column(name ="document_file_type")
    private String documentFileType;

    @Column(name ="update_user")
    private String updateUser;

    @Column(name ="create_user")
    private String createUser;

    private DocumentMetadata metadata;
    private DocumentType documentType;

    private List<AnalyzeDTO> analyzeList;

    public DocumentAnalyzeDTO(UUID id, int pageCount, String name, long size, String state, String createDate, String createTime, String updateDate, String updateTime, String status, String documentFileType , String createUser, String updateUser,List<AnalyzeDTO> analyzeList,DocumentMetadata metadata,DocumentType documentType) {
        this.id = id;
        this.pageCount = pageCount;
        this.name = name;
        this.size = size;
        this.createDate=createDate;
        this.createTime=createTime;
        this.state=state;
        this.updateDate=updateDate;
        this.updateTime=updateTime;
        this.status=status;
        this.documentFileType=documentFileType;
        this.createUser=createUser;
        this.updateUser=updateUser;
        this.analyzeList=analyzeList;
        this.metadata=metadata;
        this.documentType=documentType;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getDocumentFileType() {
        return documentFileType;
    }

    public void setDocumentFileType(String documentFileType) {
        this.documentFileType = documentFileType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public List<AnalyzeDTO> getAnalyzeList() {
        return analyzeList;
    }

    public void setAnalyzeList(List<AnalyzeDTO> analyzeList) {
        this.analyzeList = analyzeList;
    }

    public DocumentMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(DocumentMetadata metadata) {
        this.metadata = metadata;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
}

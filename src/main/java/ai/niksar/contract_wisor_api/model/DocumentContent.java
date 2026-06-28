package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT)
public class DocumentContent {
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

    @Column(name = "parse_state")
    private String parseState;

    @Column(name = "analyze_state")
    private String analyzeState;

    @Lob
    @Column(name = "content")
    @JsonIgnore
    private byte[] content;

    @Lob
    @Column(name = "text_content")
    @JsonIgnore
    private String textContent;

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

    @Column(name = "document_type_id")
    private UUID documentTypeId;

    @Column(name = "metadata_id")
    private UUID metadataId;

    @Column(name ="document_file_type")
    private String documentFileType;

    @Column(name ="update_user")
    private String updateUser;

    @Column(name ="create_user")
    private String createUser;

    @ManyToOne
    @JoinColumn(name = "document_type_id", insertable = false, updatable = false)
    @JsonIgnore
    private DocumentType documentType;
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getTextContent() {
        return textContent;
    }
    public void setTextContent(String textContent) {
        this.textContent = textContent;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UUID getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(UUID documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public UUID getMetadataId() {
        return metadataId;
    }

    public void setMetadataId(UUID metadataId) {
        this.metadataId = metadataId;
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

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentFileType() {
        return documentFileType;
    }

    public void setDocumentFileType(String documentFileType) {
        this.documentFileType = documentFileType;
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












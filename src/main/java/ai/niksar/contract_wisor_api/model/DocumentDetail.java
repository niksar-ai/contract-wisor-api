package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.dto.DocumentMetadataDTO;
import ai.niksar.contract_wisor_api.dto.DocumentPermissionDTO;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT)
public class DocumentDetail {
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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "document_type_id")
    private DocumentType documentType;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "metadata_id")
    @JsonIgnore
    private DocumentMetadata documentMetadata;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "create_user", referencedColumnName = "username")
    private UserModel createUser;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "update_user", referencedColumnName = "username")
    private UserModel updateUser;

    @Transient
    private DocumentPermissionDTO permission;
    @Transient
    private boolean isFavorite;
    @Transient
    private DocumentMetadataDTO metadata;

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

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }

    public DocumentMetadataDTO getMetadata() {
        return metadata;
    }

    public void setMetadata(DocumentMetadataDTO metadata) {
        this.metadata = metadata;
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

    public String getDocumentFileType() {
        return documentFileType;
    }

    public void setDocumentFileType(String documentFileType) {
        this.documentFileType = documentFileType;
    }

    public UserModel getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserModel createUser) {
        this.createUser = createUser;
    }

    public UserModel getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(UserModel updateUser) {
        this.updateUser = updateUser;
    }

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public DocumentPermissionDTO getPermission() {
        return permission;
    }

    public void setPermission(DocumentPermissionDTO permission) {
        this.permission = permission;
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

    public DocumentMetadata getDocumentMetadata() {
        return documentMetadata;
    }

    public void setDocumentMetadata(DocumentMetadata documentMetadata) {
        this.documentMetadata = documentMetadata;
    }
}












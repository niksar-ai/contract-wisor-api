package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.Tables;

import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT_COMMENT)
public class DocumentComment {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="comment",columnDefinition = "TEXT")
    private String comment;

    @Column(name="create_time")
    private String createTime;

    @Column(name="create_date")
    private String createDate;

    @Column(name="document_id")
    private UUID documentId;

    @Column(name="update_time")
    private String updateTime;

    @Column(name="update_date")
    private String updateDate;

    @Column(name="comment_user" , insertable = false, updatable = false)
    @JsonIgnore
    private String commentUser;

    @Column(name="is_edited")
    private Boolean isEdited;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "comment_user", referencedColumnName = "username")
    private UserModel User;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public UUID getDocumentId() {
        return documentId;
    }

    public void setDocumentId(UUID documentId) {
        this.documentId = documentId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getCommentUser() {
        return commentUser;
    }

    public void setCommentUser(String commentUser) {
        this.commentUser = commentUser;
    }
    public Boolean getEdited() {
        return isEdited;
    }

    public void setEdited(Boolean edited) {
        isEdited = edited;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public UserModel getUser() {
        return User;
    }

    public void setUser(UserModel user) {
        User = user;
    }
}

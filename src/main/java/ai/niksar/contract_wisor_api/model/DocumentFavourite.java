package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.Tables;

import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT_FAVORITE, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"document_id", "create_user"})
})
public class DocumentFavourite {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="create_time")
    private String createTime;

    @Column(name="create_date")
    private String createDate;

    @Column(name="document_id", nullable = false)
    private UUID documentId;

    @Column(name="create_user", nullable = false)
    private String createUser;


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

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
}

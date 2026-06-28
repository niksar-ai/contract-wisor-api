package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.Tables;

import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT_VIEW)
public class DocumentView {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="view_date")
    private String viewDate;

    @Column(name="view_time")
    private String viewTime;

    @Column(name="view_user")
    private String viewUser;

    @Column(name="document_id")
    private UUID viewDocumentId;


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getViewDate() {
        return viewDate;
    }

    public void setViewDate(String viewDate) {
        this.viewDate = viewDate;
    }

    public String getViewTime() {
        return viewTime;
    }

    public void setViewTime(String viewTime) {
        this.viewTime = viewTime;
    }

    public String getViewUser() {
        return viewUser;
    }

    public void setViewUser(String viewUser) {
        this.viewUser = viewUser;
    }

    public UUID getViewDocumentId() {
        return viewDocumentId;
    }

    public void setViewDocumentId(UUID viewDocumentId) {
        this.viewDocumentId = viewDocumentId;
    }
}

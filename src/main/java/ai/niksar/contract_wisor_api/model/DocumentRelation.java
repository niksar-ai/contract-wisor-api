package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT_RELATIONS ,uniqueConstraints = @UniqueConstraint(columnNames = {"parent_id", "child_id"}))
public class DocumentRelation {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "parent_id")
    private UUID parentId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "child_id")
    private Document child;

    @Column(name = "relation_type")
    private String relationType;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Document getChild() {
        return child;
    }

    public void setChild(Document child) {
        this.child = child;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }
}


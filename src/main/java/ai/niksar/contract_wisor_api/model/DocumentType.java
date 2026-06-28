package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Tables.DOCUMENT_TYPE)
public class DocumentType {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_id")
    private UUID parentId;
    @OneToMany(mappedBy = "documentType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Document> documents;
    @Transient
    private List<DocumentType> children = new ArrayList<>();
    @Transient
    private String nodeType;
    // Getters and Setters
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

    public UUID getParentId() {
        return parentId;
    }

    public void setParentId(UUID parentId) {
        this.parentId = parentId;
    }

    public List<DocumentType> getChildren() {
        return children;
    }

    public void setChildren(List<DocumentType> children) {
        this.children = children;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}

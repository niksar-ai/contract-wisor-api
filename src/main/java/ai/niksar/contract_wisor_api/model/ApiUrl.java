package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Updatable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_definition", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"url", "method","description"})
})
public class ApiUrl {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "name")
    private String name;

    @Column(name = "method")
    private String method;

    @Column(name ="code",unique = true)
    private String code;

    @Column(name = "controller_class",columnDefinition = "TEXT")
    private String controller;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at")
    private LocalDateTime createDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "updated_at")
    private LocalDateTime updateDate;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty
    private transient Boolean isRoleAssign;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate=LocalDateTime.now();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public Boolean getIsRoleAssign() {
        return isRoleAssign;
    }

    public void setRoleAssign(Boolean roleAssign) {
        isRoleAssign = roleAssign;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(LocalDateTime updateDate) {
        this.updateDate = updateDate;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
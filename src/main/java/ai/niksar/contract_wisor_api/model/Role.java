package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.util.UUID;

@Entity
@Table(name = Tables.ROLE)
public class Role {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "is_admin")
    private boolean isAdmin;

    // Getters and Setters

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
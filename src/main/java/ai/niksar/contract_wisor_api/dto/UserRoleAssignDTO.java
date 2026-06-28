package ai.niksar.contract_wisor_api.dto;

import java.util.List;
import java.util.UUID;

public class UserRoleAssignDTO {
    private UUID userId;
    private List<UUID> roleIds;

    // Getters ve Setters
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<UUID> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<UUID> roleIds) {
        this.roleIds = roleIds;
    }
}

package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class UserRoleDTO {
    private UUID id;
    private String expiryDate;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}

package ai.niksar.contract_wisor_api.dto;

import java.time.LocalDateTime;

public class BlockedUserDTO {

    private String reason;
    private LocalDateTime unblockedAt;


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getUnblockedAt() {
        return unblockedAt;
    }

    public void setUnblockedAt(LocalDateTime unblockedAt) {
        this.unblockedAt = unblockedAt;
    }
}

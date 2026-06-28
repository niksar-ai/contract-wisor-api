package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class DocumentChildDTO {
    private UUID    childId;
    private String  relationType;

    public UUID getChildId() {
        return childId;
    }
    public void setChildId(UUID childId) {
        this.childId = childId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }
}

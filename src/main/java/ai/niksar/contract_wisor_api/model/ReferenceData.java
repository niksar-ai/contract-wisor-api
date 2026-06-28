package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;
import ai.niksar.contract_wisor_api.util.ReferenceDataItem;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = Tables.REFERENCE_DATA)
public class ReferenceData {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "ref_id", nullable = false)
    private String refId;

    @ElementCollection
    @CollectionTable(name = Tables.REFERENCE_DATA_CONTEXT, joinColumns = @JoinColumn(name = "reference_data_id"))
    @Column(columnDefinition = "jsonb", nullable = false)
    private List<ReferenceDataItem> data;

    // Getter and Setter methods
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public List<ReferenceDataItem> getData() {
        return data;
    }

    public void setData(List<ReferenceDataItem> data) {
        this.data = data;
    }
}

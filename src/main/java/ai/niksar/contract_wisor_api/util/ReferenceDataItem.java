package ai.niksar.contract_wisor_api.util;

import jakarta.persistence.Embeddable;

@Embeddable
public class ReferenceDataItem {
    private String key;
    private String label;
    private String filter;

    // Getter and Setter methods
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
}

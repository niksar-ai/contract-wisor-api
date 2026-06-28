package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class DocumentTypeCountDTO {
    private UUID id;
    private String name;

    private long countDocument;
    // Getters and Setters

    public DocumentTypeCountDTO(UUID id,String name, long countDocument) {
        this.id=id;
        this.name = name;
        this.countDocument = countDocument;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getCountDocument() {
        return countDocument;
    }

    public void setCountDocument(long countDocument) {
        this.countDocument = countDocument;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

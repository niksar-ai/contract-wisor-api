package ai.niksar.contract_wisor_api.dto;

import java.util.UUID;

public class DocumentTypeSizeDTO {
    private UUID id;
    private String name;

    private long sizeDocument;
    // Getters and Setters

    public DocumentTypeSizeDTO(UUID id,String name, long sizeDocument) {
        this.id=id;
        this.name = name;
        this.sizeDocument = sizeDocument;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public long getSizeDocument() {
        return sizeDocument;
    }

    public void setSizeDocument(long sizeDocument) {
        this.sizeDocument = sizeDocument;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

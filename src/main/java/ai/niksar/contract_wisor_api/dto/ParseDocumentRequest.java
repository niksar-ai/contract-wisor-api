package ai.niksar.contract_wisor_api.dto;

// Inner class for request body
public class ParseDocumentRequest {
    private byte[] documentData;
    private String fileType;

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}

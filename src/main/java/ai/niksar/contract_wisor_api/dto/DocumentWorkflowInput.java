package ai.niksar.contract_wisor_api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentWorkflowInput {
    @JsonProperty("jobId")
    private String jobId;

    @JsonProperty("docId")
    private String docId;

    public DocumentWorkflowInput(@JsonProperty("jobId") String jobId, @JsonProperty("docId") String docId) {
        this.jobId = jobId;
        this.docId = docId;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }
}

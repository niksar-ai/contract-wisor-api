package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DocumentPermissionDTO {
    @JsonProperty("isParse")
    private boolean parse;

    @JsonProperty("isAnalyze")
    private boolean analyze;

    @JsonProperty("isDelete")
    private boolean delete;

    @JsonProperty("isDownload")
    private boolean download;

    @JsonProperty("isComplete")
    private boolean complete;

    @JsonProperty("isReopen")
    private boolean reOpen;

    @JsonProperty("isMetadataSave")
    private boolean metadataSave;

    @JsonProperty("isRelationSave")
    private boolean relationSave;

    @JsonProperty("isRelationDelete")
    private boolean relationDelete;

    @JsonProperty("isRelationView")
    private boolean relationView;



    // Getter ve Setter'lar
    public boolean isParse() {
        return parse;
    }

    public void setParse(boolean parse) {
        this.parse = parse;
    }

    public boolean isAnalyze() {
        return analyze;
    }

    public void setAnalyze(boolean analyze) {
        this.analyze = analyze;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isDownload() {
        return download;
    }

    public void setDownload(boolean download) {
        this.download = download;
    }

    public boolean getComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean getReOpen() {
        return reOpen;
    }

    public void setReOpen(boolean reOpen) {
        this.reOpen = reOpen;
    }

    public boolean isMetadataSave() {
        return metadataSave;
    }

    public void setMetadataSave(boolean metadataSave) {
        this.metadataSave = metadataSave;
    }

    public boolean isRelationSave() {
        return relationSave;
    }

    public void setRelationSave(boolean relationSave) {
        this.relationSave = relationSave;
    }

    public boolean isRelationDelete() {
        return relationDelete;
    }

    public void setRelationDelete(boolean relationDelete) {
        this.relationDelete = relationDelete;
    }

    public boolean isRelationView() {
        return relationView;
    }

    public void setRelationView(boolean relationView) {
        this.relationView = relationView;
    }
}

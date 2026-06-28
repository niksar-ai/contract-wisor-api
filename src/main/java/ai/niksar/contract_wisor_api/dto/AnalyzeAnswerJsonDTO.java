package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
public class AnalyzeAnswerJsonDTO {
    private String version;
    private List<String> tags;
    private Map<String, String> meta;
    @JsonProperty("short_answer")
    private AnalyzeShortAnswerDTO shortAnswer;
    @JsonProperty("long_answer")
    private String longAnswer;
    @JsonProperty("summary_answer")
    private String summaryAnswer;
    private Map<String, Object> debug;
    @JsonProperty("stats")
    private Map<String, Object> stats;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public String getLongAnswer() {
        return longAnswer;
    }

    public void setLongAnswer(String longAnswer) {
        this.longAnswer = longAnswer;
    }

    public String getSummaryAnswer() {
        return summaryAnswer;
    }

    public void setSummaryAnswer(String summaryAnswer) {
        this.summaryAnswer = summaryAnswer;
    }

    public Map<String, Object> getDebug() {
        return debug;
    }

    public void setDebug(Map<String, Object> debug) {
        this.debug = debug;
    }

    public AnalyzeShortAnswerDTO getShortAnswer() {
        return shortAnswer;
    }

    public void setShortAnswer(AnalyzeShortAnswerDTO shortAnswer) {
        this.shortAnswer = shortAnswer;
    }

    public Map<String, Object> getStats() {
        return stats;
    }

    public void setStats(Map<String, Object> stats) {
        this.stats = stats;
    }


    // Getters and Setters
}

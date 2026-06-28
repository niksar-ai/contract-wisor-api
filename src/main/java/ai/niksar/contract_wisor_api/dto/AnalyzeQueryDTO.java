package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class AnalyzeQueryDTO {
    private Integer id;
    private String code;
    @JsonProperty("data_type")
    private String dataType;
    @JsonProperty("question_title")
    private String questionTitle;
    @JsonProperty("question_desc")
    private String questionDesc;
    private List<AnalyzeAnswerJsonDTO> answers;
    @JsonProperty("stats")
    private Map<String, Object> stats;
    @JsonProperty("date_aware")
    private boolean dateAware;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public List<AnalyzeAnswerJsonDTO> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnalyzeAnswerJsonDTO> answers) {
        this.answers = answers;
    }

    public boolean getDateAware() {
        return dateAware;
    }

    public void setDateAware(boolean dateAware) {
        this.dateAware = dateAware;
    }

    // Getters and Setters
}

package ai.niksar.contract_wisor_api.dto;


import jakarta.persistence.Transient;

import java.util.UUID;

public class AnalyzeQuestionDTO {
    private UUID id;
    private String code;
    private String questionDesc;
    private String questionTitle;
    @Transient
    private AnalyzeAnswerDTO answer;

    public AnalyzeQuestionDTO(UUID id, String code, String questionDesc, String questionTitle) {
        this.id = id;
        this.code = code;
        this.questionDesc = questionDesc;
        this.questionTitle = questionTitle;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getQuestionDesc() {
        return questionDesc;
    }

    public void setQuestionDesc(String questionDesc) {
        this.questionDesc = questionDesc;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }


    public AnalyzeAnswerDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnalyzeAnswerDTO answer) {
        this.answer = answer;
    }
}

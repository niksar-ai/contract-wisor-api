package ai.niksar.contract_wisor_api.dto;


import java.util.UUID;

public class AnalyzeAcceptedQueryDTO {
    private String code;
    private AnalyzeAnswerAcceptedJsonDTO answer;
    private UUID id;
    private String questionDesc;
    private String questionTitle;
    private String questionDataType;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public AnalyzeAnswerAcceptedJsonDTO getAnswer() {
        return answer;
    }

    public void setAnswer(AnalyzeAnswerAcceptedJsonDTO answer) {
        this.answer = answer;
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

    public String getQuestionDataType() {
        return questionDataType;
    }

    public void setQuestionDataType(String questionDataType) {
        this.questionDataType = questionDataType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

package ai.niksar.contract_wisor_api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import ai.niksar.contract_wisor_api.util.FlexibleValueDeserializer;

public class AnalyzeShortAnswerDTO {
    @JsonProperty("answer_generated")
    private boolean answerGenerated;
    private String type;
    private String text;
    @JsonDeserialize(using = FlexibleValueDeserializer.class)
    private Object value;

    public boolean isAnswerGenerated() {
        return answerGenerated;
    }

    public void setAnswerGenerated(boolean answerGenerated) {
        this.answerGenerated = answerGenerated;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

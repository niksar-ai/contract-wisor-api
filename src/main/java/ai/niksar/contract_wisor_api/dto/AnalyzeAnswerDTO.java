package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.Transient;

import java.math.BigDecimal;
import java.util.List;

public class AnalyzeAnswerDTO {
    private String shortAnswerType;
    private String longAnswer;
    private String summaryAnswer;
    @JsonIgnore
    private String shortAnswerJsonValue;
    private boolean shortAnswerGenerated;
    @Transient
    private Object shortAnswerValue;
    private BigDecimal successRate;
    public AnalyzeAnswerDTO(String shortAnswerType, String longAnswer, String summaryAnswer,
                            String shortAnswerJsonValue, boolean shortAnswerGenerated,BigDecimal successRate) {
        this.shortAnswerType = shortAnswerType;
        this.longAnswer = longAnswer;
        this.summaryAnswer = summaryAnswer;
        this.shortAnswerJsonValue = shortAnswerJsonValue;
        this.shortAnswerGenerated = shortAnswerGenerated;
        this.successRate=successRate;
        parseShortAnswerValue();
    }
    public void parseShortAnswerValue() {
        if (shortAnswerJsonValue != null && !shortAnswerJsonValue.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(shortAnswerJsonValue);

                if (jsonNode.isNumber()) {
                    shortAnswerValue = jsonNode.numberValue();
                } else if (jsonNode.isTextual()) {
                    shortAnswerValue = jsonNode.textValue();
                } else if (jsonNode.isArray()) {
                    shortAnswerValue = objectMapper.convertValue(jsonNode, List.class);
                }
                else if(jsonNode.isBoolean()){
                    shortAnswerValue=jsonNode.booleanValue();
                }else if (jsonNode.isNull()) {
                    shortAnswerValue = null;
                } else {
                    shortAnswerValue = jsonNode.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


    public String getShortAnswerType() {
        return shortAnswerType;
    }

    public void setShortAnswerType(String shortAnswerType) {
        this.shortAnswerType = shortAnswerType;
    }


    public boolean isShortAnswerGenerated() {
        return shortAnswerGenerated;
    }

    public void setShortAnswerGenerated(boolean shortAnswerGenerated) {
        this.shortAnswerGenerated = shortAnswerGenerated;
    }

    public String getShortAnswerJsonValue() {
        return shortAnswerJsonValue;
    }

    public void setShortAnswerJsonValue(String shortAnswerJsonValue) {
        this.shortAnswerJsonValue = shortAnswerJsonValue;
    }

    public Object getShortAnswerValue() {
        return shortAnswerValue;
    }

    public void setShortAnswerValue(Object shortAnswerValue) {
        this.shortAnswerValue = shortAnswerValue;
    }

    public BigDecimal getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(BigDecimal successRate) {
        this.successRate = successRate;
    }
}


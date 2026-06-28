package ai.niksar.contract_wisor_api.dto;

public class AnalyzeAnswerAcceptedJsonDTO {
    private String summaryAnswer;
    private String longAnswer;
    private Object shortAnswerValue;
    private String shortAnswerType;
    private boolean shortAnswerGenerated;

    public String getSummaryAnswer() {
        return summaryAnswer;
    }

    public void setSummaryAnswer(String summaryAnswer) {
        this.summaryAnswer = summaryAnswer;
    }

    public String getLongAnswer() {
        return longAnswer;
    }

    public void setLongAnswer(String longAnswer) {
        this.longAnswer = longAnswer;
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

    public Object getShortAnswerValue() {
        return shortAnswerValue;
    }

    public void setShortAnswerValue(Object shortAnswerValue) {
        this.shortAnswerValue = shortAnswerValue;
    }
}

package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "analyze_question")
public class AnalyzeQuestion {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name="code")
    private String code;

    @Column(name = "data_type")
    private String dataType;

    @Column(name ="question_desc",columnDefinition = "TEXT")
    private String questionDesc;
    @Column(name ="question_title",columnDefinition = "TEXT")
    private String questionTitle;

    @Column(name="document_type_id_list")
    @JsonIgnore
    private List<UUID> documentTypeList;

    @Column(name = "order_no")
    private Integer orderNo;

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


    public List<UUID> getDocumentTypeList() {
        return documentTypeList;
    }

    public void setDocumentTypeList(List<UUID> documentTypeList) {
        this.documentTypeList = documentTypeList;
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }
}

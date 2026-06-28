package ai.niksar.contract_wisor_api.dto;

import ai.niksar.contract_wisor_api.model.DocumentRelation;

import java.util.List;

public class DocumentRelationListDTO {
    private Integer numPages;
    private Integer totalRecords;
    private Integer current;
    private Integer perPage;
    private List<DocumentRelation> records;

    public Integer getNumPages() {
        return numPages;
    }

    public void setNumPages(Integer numPages) {
        this.numPages = numPages;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public List<DocumentRelation> getRecords() {
        return records;
    }

    public void setRecords(List<DocumentRelation> records) {
        this.records = records;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }
}

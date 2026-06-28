package ai.niksar.contract_wisor_api.dto;

import java.util.List;

public class DocumentAnalyzeListDTO {
    private Integer numPages;
    private Integer totalRecords;
    private Integer current;
    private Integer perPage;
    private List<DocumentAnalyzeDTO> records;
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
    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public List<DocumentAnalyzeDTO> getRecords() {
        return records;
    }

    public void setRecords(List<DocumentAnalyzeDTO> records) {
        this.records = records;
    }
}

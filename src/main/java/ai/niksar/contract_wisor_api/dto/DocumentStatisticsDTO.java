package ai.niksar.contract_wisor_api.dto;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public class DocumentStatisticsDTO {
    private long totalDocumentCount;
    private BigInteger totalDocumentSize;
    private double metadataRate;
    private List<Map<String, Object>> stateStatistic;


    public long getTotalDocumentCount() {
        return totalDocumentCount;
    }

    public void setTotalDocumentCount(long totalDocumentCount) {
        this.totalDocumentCount = totalDocumentCount;
    }

    public BigInteger getTotalDocumentSize() {
        return totalDocumentSize;
    }

    public void setTotalDocumentSize(BigInteger totalDocumentSize) {
        this.totalDocumentSize = totalDocumentSize;
    }

    public double getMetadataRate() {
        return metadataRate;
    }

    public void setMetadataRate(double metadataRate) {
        this.metadataRate = metadataRate;
    }

    public List<Map<String, Object>> getStateStatistic() {
        return stateStatistic;
    }

    public void setStateStatistic(List<Map<String, Object>> stateStatistic) {
        this.stateStatistic = stateStatistic;
    }
}

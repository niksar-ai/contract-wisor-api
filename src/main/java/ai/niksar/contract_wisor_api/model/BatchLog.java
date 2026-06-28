package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = Tables.BATCH_LOG)
public class BatchLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = BatchLogTable.BATCH_ID, nullable = false)
    private UUID batchId;

    @Column(name = BatchLogTable.STATUS, nullable = false)
    private String status;

    @Column(name = BatchLogTable.ERROR_MESSAGE)
    private String errorMessage;

    @Column(name = BatchLogTable.LOG_TIME, nullable = false)
    private LocalDateTime logTime;

    @Column(name = BatchLogTable.START_TIME)
    private LocalDateTime startTime;

    @Column(name = BatchLogTable.END_TIME)
    private LocalDateTime endTime;

    @Column(name = BatchLogTable.DURATION)
    private Long duration;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBatchId() {
        return batchId;
    }

    public void setBatchId(UUID batchId) {
        this.batchId = batchId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getLogTime() {
        return logTime;
    }

    public void setLogTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}

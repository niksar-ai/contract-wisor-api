package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = Tables.BATCH_DEFINITION)
public class BatchDefinition {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = BatchDefinitionTable.SERVICE_NAME, nullable = false)
    private String serviceName;

    @Column(name = BatchDefinitionTable.BATCH_NAME, nullable = false)
    private String batchName;

    @Column(name = BatchDefinitionTable.CRON_TIME, nullable = false)
    private String cronTime;

    @Column(name = BatchDefinitionTable.CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = BatchDefinitionTable.UPDATED_AT)
    private LocalDateTime updatedAt;

    @Column(name = BatchDefinitionTable.STATUS, nullable = false)
    private String status;

    @Column(name = BatchDefinitionTable.MAX_RETRY_COUNT)
    private Integer maxRetryCount;

    @Column(name = BatchDefinitionTable.RETRY_INTERVAL)
    private Integer retryInterval;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        this.batchName = batchName;
    }

    public String getCronTime() {
        return cronTime;
    }

    public void setCronTime(String cronTime) {
        this.cronTime = cronTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getMaxRetryCount() {
        return maxRetryCount;
    }

    public void setMaxRetryCount(Integer maxRetryCount) {
        this.maxRetryCount = maxRetryCount;
    }

    public Integer getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }
}
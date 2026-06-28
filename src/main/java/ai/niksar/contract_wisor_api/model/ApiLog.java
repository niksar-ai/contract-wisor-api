package ai.niksar.contract_wisor_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "api_logs")
public class ApiLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "api_url", nullable = false)
    private String apiUrl;

    @Column(name = "method", nullable = false)
    private String method;

    @Lob
    @Column(name = "query_params")
    private String queryParams;

    @Lob
    @Column(name = "request_body")
    private String requestBody;

    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Lob
    @Column(name = "response_body")
    private String responseBody;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @Column(name = "response_status_code")
    private String statusCode;

    @Column(name = "ip")
    private String ip;

    @Column(name = "username")
    private String username;

    @Column(name = "session_id")
    private UUID sessionId;

    @Column(name = "device_info")
    private String deviceInfo;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getQueryParams() {
        return queryParams;
    }

    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public LocalDateTime getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(LocalDateTime responseTime) {
        this.responseTime = responseTime;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
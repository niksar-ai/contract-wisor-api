package ai.niksar.contract_wisor_api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.UUID;

public class FTPServerDTO {

    private UUID id;

    private String server;

    private String serverName;

    private String port;

    private String ftpUser;

    private String password;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastConnectAt;

    public FTPServerDTO(UUID id, String server, String serverName, String port, String ftpUser, String password, LocalDateTime lastConnectAt){
        this.id                 = id;
        this.server             = server;
        this.serverName         = serverName;
        this.port               = port;
        this.ftpUser            = ftpUser;
        this.password           = password;
        this.lastConnectAt      = lastConnectAt;
    }

    public FTPServerDTO(){ }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpName) {
        this.ftpUser = ftpName;
    }

    public LocalDateTime getLastConnectAt() {
        return lastConnectAt;
    }

    public void setLastConnectAt(LocalDateTime lastConnectAt) {
        this.lastConnectAt = lastConnectAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
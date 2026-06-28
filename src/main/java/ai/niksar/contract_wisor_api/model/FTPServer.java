package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Updatable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name ="ftp_server")
public class FTPServer {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(name ="server", nullable = false)
    private String server;

    @Column(name ="server_name", nullable = false)
    private String serverName;

    @Column(name ="port", nullable = false)
    private String port;

    @Column(name ="username", nullable = false)
    private String username;

    @Column(name ="ftp_user", nullable = false)
    private String ftpUser;

    @Column(name ="password", nullable = false)
    private String password;

    @Column(name = "status")
    private String status;

    @Column(name = "create_user")
    private String createUser;

    @Column(name = "update_user")
    private String updateUser;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @Column(name = "last_connect_at", nullable = false)
    private LocalDateTime lastConnectAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpName) {
        this.ftpUser = ftpName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String userName) {
        this.username = userName;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getLastConnectAt() {
        return lastConnectAt;
    }

    public void setLastConnectAt(LocalDateTime lastConnectAt) {
        this.lastConnectAt = lastConnectAt;
    }
}
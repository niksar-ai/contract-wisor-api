package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.*;
import ai.niksar.contract_wisor_api.util.Updatable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = Tables.USERS)
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = UserTable.NAME, nullable = false, unique = true)
    private String name;

    @Column(name = UserTable.SURNAME, nullable = false, unique = true)
    private String surname;

    @Column(name = UserTable.USERNAME, nullable = false, unique = true)
    private String username;

    @Column(name = UserTable.EMAIL, nullable = false, unique = true)
    private String email;

    @Column(name = UserTable.PASSWORD, nullable = false)
    private String password;

    @Column(name = UserTable.NAME_TITLE, nullable = false)
    private String nameTitle;

    @Column(name = UserTable.STATUS, nullable = false)
    private String status;
    @JsonIgnore
    @Column(name = UserTable.CREATE_DATE)
    private String createDate;
    @JsonIgnore
    @Column(name = UserTable.CREATE_TIME)
    private String createTime;


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @JsonIgnore
    @Column(name = UserTable.LAST_LOGIN_TIME)
    private LocalDateTime lastLoginTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @JsonIgnore
    @Column(name = UserTable.LAST_FAILURE_LOGIN_TIME)
    private LocalDateTime lastFailureLoginTime;

    @Column(name = UserTable.AVATAR)
    private String avatar;

    @Column(name = UserTable.IS_PASSWORD_CHANGE)
    private boolean isPasswordChange;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Updatable
    @Column(name = UserTable.PASSWORD_EXPIRY_MONTH)
    private LocalDateTime passwordExpiryMonth;

    //Getter and Setter methods

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNameTitle() {
        return nameTitle;
    }

    public void setNameTitle(String nameTitle) {
        this.nameTitle = nameTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public LocalDateTime getLastFailureLoginTime() {
        return lastFailureLoginTime;
    }

    public void setLastFailureLoginTime(LocalDateTime lastFailureLoginTime) {
        this.lastFailureLoginTime = lastFailureLoginTime;
    }

    public boolean isPasswordChange() {
        return isPasswordChange;
    }

    public void setPasswordChange(boolean passwordChange) {
        isPasswordChange = passwordChange;
    }

    public LocalDateTime getPasswordExpiryMonth() {
        return passwordExpiryMonth;
    }

    public void setPasswordExpiryMonth(LocalDateTime passwordExpiryMonth) {
        this.passwordExpiryMonth = passwordExpiryMonth;
    }
}
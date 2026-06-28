package ai.niksar.contract_wisor_api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ai.niksar.contract_wisor_api.util.Constants.Tables;
import ai.niksar.contract_wisor_api.util.Constants.UserTable;
import ai.niksar.contract_wisor_api.util.Updatable;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = Tables.USERS)
public class UserModel {

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

    @Column(name = UserTable.NAME_TITLE, nullable = false)
    private String nameTitle;

    @Column(name = UserTable.STATUS, nullable = false)
    private String status;

    @Column(name = UserTable.AVATAR)
    private String avatar;

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

}
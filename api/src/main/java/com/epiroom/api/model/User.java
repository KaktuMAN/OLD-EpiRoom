package com.epiroom.api.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String mail;

    @Column(name = "name")
    private String name;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "first_login")
    private Date firstLogin;

    @Column(name = "last_login")
    private Date lastLogin;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPermission> userPermissions;

    public User() {
    }

    public User(String mail, String name) {
        this.mail = mail;
        this.name = name;
        this.firstLogin = new Date();
        this.lastLogin = new Date();
    }

    public String getMail() {
        return mail;
    }

    public String getName() {
        return name;
    }

    public String getApiKey() {
        return apiKey;
    }

    public List<Permission> getPermissions() {
        if (userPermissions == null)
            return List.of();
        return userPermissions.stream().map(UserPermission::getPermission).collect(Collectors.toList());
    }

    public boolean hasPermission(String permission) {
        return getPermissions().stream().anyMatch(p -> p.getPermission().equals(permission));
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}

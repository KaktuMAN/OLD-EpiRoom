package com.epiroom.api.model;

import jakarta.persistence.*;

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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPermission> userPermissions;

    public User() {
    }

    public User(String mail) {
        this.mail = mail;
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
        return userPermissions.stream().map(UserPermission::getPermission).collect(Collectors.toList());
    }

    public boolean hasPermission(String permission) {
        return getPermissions().stream().anyMatch(p -> p.getPermission().equals(permission));
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}

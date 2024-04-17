package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @NotNull
    private String mail;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "first_login")
    @NotNull
    private Date firstLogin;

    @Setter
    @Column(name = "last_login")
    @NotNull
    private Date lastLogin;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<UserPermission> userPermissions;

    public User(String mail, String name) {
        this.mail = mail;
        this.name = name;
        this.firstLogin = new Date();
        this.lastLogin = new Date();
    }

    public List<Permission> getPermissions() {
        if (userPermissions == null)
            return List.of();
        return userPermissions.stream().map(UserPermission::getPermission).collect(Collectors.toList());
    }
}

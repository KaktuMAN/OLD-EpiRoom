package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_permissions")
public class UserPermission {
    @Id
    @Column(name = "mail")
    @NotNull
    private String mail;

    @Id
    @Column(name = "permission_id")
    @NotNull
    private int permissionId;

    @ManyToOne
    @JoinColumn(name = "mail", referencedColumnName = "mail")
    private User user;

    @ManyToOne
    @JoinColumn(name = "permission_id", referencedColumnName = "id")
    private Permission permission;

        @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPermission that = (UserPermission) o;
        return permissionId == that.permissionId && mail.equals(that.mail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mail, permissionId);
    }
}
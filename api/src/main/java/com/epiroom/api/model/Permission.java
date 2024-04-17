package com.epiroom.api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @SequenceGenerator(name="permissions_id_seq", sequenceName="permissions_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="permissions_id_seq")
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "permission", nullable = false)
    private String permission;
}
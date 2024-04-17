package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "campus")
public class Campus {
    @Id
    @NotNull
    @Size(min = 3, max = 3)
    private String code;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "main_floor")
    private Integer mainFloorId;

    @Column(name = "auto_login")
    @Size(min = 45, max = 45)
    private String autoLogin;

    @Column(name = "jenkins_token")
    @Size(min = 36, max = 36)
    private String jenkinsToken;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "campusCode", cascade = CascadeType.ALL)
    private List<Floor> floors;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "campusCode", cascade = CascadeType.ALL)
    private List<Room> rooms;
}
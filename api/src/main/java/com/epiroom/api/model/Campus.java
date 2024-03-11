package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "campus")
public class Campus {
    @Id
    @NotNull
    @Size(min = 3, max = 3)
    private String code;

    @Column(name = "name")
    @NotNull
    @Size(max = 50)
    private String name;

    @Column(name = "main_floor")
    private int mainFloor;

    public Campus() {
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getMainFloor() {
        return mainFloor;
    }
}

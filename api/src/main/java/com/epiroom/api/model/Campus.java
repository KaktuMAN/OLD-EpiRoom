package com.epiroom.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "campus")
public class Campus {
    @Id
    private String code;

    @Column(name = "name")
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

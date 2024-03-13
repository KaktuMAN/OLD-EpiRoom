package com.epiroom.api.model;

import com.epiroom.api.model.dto.campus.SimpleCampus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "campus")
public class Campus {
    @Id
    @NotNull
    @Size(min = 3, max = 3)
    private String code;

    @Column(name = "name")
    @Size(max = 50)
    private String name;

    @Column(name = "main_floor")
    private Integer mainFloorId;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "campusCode", cascade = CascadeType.ALL)
    private List<Floor> floors;

    public Campus() {
    }

    public Campus(SimpleCampus campus) {
        this.code = campus.getCode().toUpperCase();
        this.name = campus.getName();
        this.mainFloorId = null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getMainFloorId() {
        return mainFloorId;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public void setMainFloorId(Integer mainFloorId) {
        this.mainFloorId = mainFloorId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
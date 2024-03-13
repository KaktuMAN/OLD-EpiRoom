package com.epiroom.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "floors")
public class Floor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "campus_code")
    private String campusCode;

    @Column(name = "floor")
    private int floor;

    @Column(name = "name")
    private String name;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    public Floor() {
    }

    public Floor(Campus campus, int floor, String name) {
        this.campusCode = campus.getCode();
        this.floor = floor;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public int getFloor() {
        return floor;
    }

    public String getName() {
        return name;
    }

    public Campus getCampus() {
        return campus;
    }
}
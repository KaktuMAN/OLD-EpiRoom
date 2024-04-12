package com.epiroom.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

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
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    @JsonBackReference
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", insertable = false, updatable = false)
    private List<Room> rooms;

    public Floor() {
    }

    public Floor(Campus campus, int floor, String name) {
        this.campusCode = campus.getCode();
        this.floor = floor;
        this.name = name;
        this.campus = campus;
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

    public List<Room> getRooms() {
        return rooms;
    }

    public boolean isMainFloor() {
        if (campus.getMainFloorId() == null) {
            return false;
        }
        return campus.getMainFloorId() == id;
    }
}
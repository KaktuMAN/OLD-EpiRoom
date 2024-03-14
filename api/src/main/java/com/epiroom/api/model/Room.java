package com.epiroom.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "floor_id")
    private int floorId;
    @Column(name = "campus_code")
    private String campusCode;
    @Column(name = "type")
    private String type;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "display_status")
    private boolean display_status;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", insertable = false, updatable = false)
    private Floor floor;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    public Room() {
    }

    public int getId() {
        return id;
    }

    public int getFloorId() {
        return floorId;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public Campus getCampus() {
        return campus;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Integer getSeats() {
        return seats;
    }

    public boolean getDisplayStatus() {
        return display_status;
    }
}
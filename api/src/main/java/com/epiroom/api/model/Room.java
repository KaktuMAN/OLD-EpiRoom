package com.epiroom.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private int seats;

    @Column(name = "display_status")
    private Boolean displayStatus;

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

    public int getSeats() {
        return seats;
    }

    public Boolean getDisplayStatus() {
        return displayStatus;
    }
}

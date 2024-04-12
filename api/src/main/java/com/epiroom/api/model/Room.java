package com.epiroom.api.model;

import com.epiroom.api.model.dto.room.FullRoom;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.util.List;

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
    @Enumerated(EnumType.STRING)
    private RoomType type;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "linked_rooms",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "linked_room_id")
    )
    private List<Room> linkedRooms;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", insertable = false, updatable = false)
    private Floor floor;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    public enum RoomType {CLASSROOM, OFFICE, OPENSPACE, OTHER}

    public Room() {
    }

    public Room(FullRoom fullRoom) {
        this.floorId = fullRoom.getFloor();
        this.campusCode = fullRoom.getCampusCode();
        this.type = fullRoom.getType();
        this.name = fullRoom.getName();
        this.code = fullRoom.getCode();
        this.displayName = fullRoom.getDisplayName();
        this.seats = fullRoom.getSeats();
        this.display_status = fullRoom.getDisplayStatus();
        this.linkedRooms = null;
    }

    public void update(FullRoom fullRoom) {
        this.floorId = fullRoom.getFloor();
        this.campusCode = fullRoom.getCampusCode();
        this.type = fullRoom.getType();
        this.name = fullRoom.getName();
        this.code = fullRoom.getCode();
        this.displayName = fullRoom.getDisplayName();
        this.seats = fullRoom.getSeats();
        this.display_status = fullRoom.getDisplayStatus();
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

    public RoomType getType() {
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

    public List<Room> getLinkedRooms() {
        return linkedRooms;
    }

    public Floor getFloor() {
        return floor;
    }
}
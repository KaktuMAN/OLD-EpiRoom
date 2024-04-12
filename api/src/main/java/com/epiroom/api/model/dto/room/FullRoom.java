package com.epiroom.api.model.dto.room;

import com.epiroom.api.model.Room;

public class FullRoom {
    private final int floor;
    private final String campusCode;
    private final Room.RoomType type;
    private final String name;
    private final String code;
    private final String displayName;
    private final Integer seats;
    private final boolean displayStatus;

    public FullRoom() {
        this.floor = 0;
        this.campusCode = "";
        this.type = Room.RoomType.OTHER;
        this.name = "";
        this.code = "";
        this.displayName = "";
        this.seats = 0;
        this.displayStatus = false;
    }

    public FullRoom(Room room) {
        this.floor = room.getFloor().getFloor();
        this.campusCode = room.getCampusCode();
        this.type = room.getType();
        this.name = room.getName();
        this.code = room.getCode();
        this.displayName = room.getDisplayName();
        this.seats = room.getSeats();
        this.displayStatus = room.getDisplayStatus();
    }

    public int getFloor() {
        return floor;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public Room.RoomType getType() {
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
        return displayStatus;
    }
}
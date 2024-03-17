package com.epiroom.api.model.dto.room;

import com.epiroom.api.model.Room;

public class SimpleRoom {
    private final String name;

    private final int floor;

    private final String code;

    private final String campusCode;

    public SimpleRoom(String name, int floor, String code, String campusCode) {
        this.name = name;
        this.floor = floor;
        this.code = code;
        this.campusCode = campusCode;
    }

    public SimpleRoom(Room room) {
        this.name = room.getName();
        this.floor = room.getFloorId();
        this.code = room.getCode();
        this.campusCode = room.getCampusCode();
    }

    public String getName() {
        return name;
    }

    public int getFloor() {
        return floor;
    }

    public String getCode() {
        return code;
    }

    public String getCampusCode() {
        return campusCode;
    }
}

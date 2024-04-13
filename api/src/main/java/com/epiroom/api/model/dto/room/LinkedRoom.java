package com.epiroom.api.model.dto.room;

import com.epiroom.api.model.Room;

import java.util.List;

public class LinkedRoom {
    private final String mainRoomCode;
    private final List<String> linkedRoomCodes;

    public LinkedRoom(String mainRoomCode, List<String> linkedRoomCodes) {
        this.mainRoomCode = mainRoomCode;
        this.linkedRoomCodes = linkedRoomCodes;
    }

    public LinkedRoom(Room room) {
        this.mainRoomCode = room.getCode();
        this.linkedRoomCodes = room.getLinkedRooms().stream().map(Room::getCode).toList();
    }

    public String getMainRoomCode() {
        return mainRoomCode;
    }

    public List<String> getLinkedRoomCodes() {
        return linkedRoomCodes;
    }
}

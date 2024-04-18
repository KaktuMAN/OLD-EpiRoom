package com.epiroom.api.model.dto.room;

import com.epiroom.api.model.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleRoom {
    private final String displayName;
    private final String code;

    public SimpleRoom(Room room) {
        this.displayName = room.getDisplayName();
        this.code = room.getCode();
    }
}

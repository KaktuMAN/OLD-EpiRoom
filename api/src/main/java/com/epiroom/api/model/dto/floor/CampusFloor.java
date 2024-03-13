package com.epiroom.api.model.dto.floor;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Floor;

public class CampusFloor {
    private final int floor;

    private final String campusCode;

    private final String name;

    private final boolean isMainFloor;

    public CampusFloor(int floor, String campusCode, String name, boolean isMainFloor) {
        this.floor = floor;
        this.campusCode = campusCode;
        this.name = name;
        this.isMainFloor = isMainFloor;
    }

    public CampusFloor(Floor floor) {
        this.floor = floor.getFloor();
        this.campusCode = floor.getCampusCode();
        this.name = floor.getName();
        this.isMainFloor = floor.isMainFloor();
    }

    public CampusFloor(Campus campus, SimpleFloor floor) {
        this.floor = floor.getFloor();
        this.campusCode = campus.getCode();
        this.name = floor.getName();
        int mainFloorId = campus.getMainFloorId();
        Floor mainFloor = campus.getFloors().stream().filter(f -> f.getFloor() == mainFloorId).findFirst().orElse(null);
        this.isMainFloor = mainFloor != null && mainFloor.getFloor() == floor.getFloor();
    }

    public int getFloor() {
        return floor;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public String getName() {
        return name;
    }

    public boolean isMainFloor() {
        return isMainFloor;
    }
}

package com.epiroom.api.model.dto.floor;

public class SimpleFloor {
    private final String name;

    private final int floor;

    public SimpleFloor(int floor, String name) {
        this.floor = floor;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getFloor() {
        return floor;
    }
}

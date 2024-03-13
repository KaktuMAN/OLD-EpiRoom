package com.epiroom.api.model.dto.campus;

import com.epiroom.api.model.Campus;
import com.epiroom.api.model.Floor;

import java.util.List;

public class FullCampus {
    private final String code;
    private final String name;

    private final List<Floor> floors;

    private final Integer mainFloorId;

    public FullCampus(String code, String name, List<Floor> floors, Integer mainFloorId) {
        this.code = code;
        this.name = name;
        this.floors = floors;
        this.mainFloorId = mainFloorId;
    }

    public FullCampus(Campus campus) {
        this.code = campus.getCode();
        this.name = campus.getName();
        this.floors = campus.getFloors();
        this.mainFloorId = campus.getMainFloorId();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Floor> getFloors() {
        return floors;
    }

    public Integer getMainFloorId() {
        return mainFloorId;
    }
}

package com.epiroom.api.openapi.campus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PatchExistingCampus {
    @NotNull
    @Size(max = 50)
    private String name;

    private Integer mainFloorId;

    public String getName() {
        return name;
    }

    public Integer getMainFloorId() {
        return mainFloorId;
    }
}

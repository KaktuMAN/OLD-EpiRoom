package com.epiroom.api.openapi.campus;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostNewCampus {
    @NotNull
    @Size(min = 3, max = 3)
    private String code;
    @NotNull
    @Size(max = 50)
    private String name;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

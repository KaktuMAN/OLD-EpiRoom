package com.epiroom.api.model.dto.campus;

import com.epiroom.api.model.Campus;

public class SimpleCampus {
    private final String code;
    private final String name;

    public SimpleCampus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public SimpleCampus(Campus campus) {
        this.code = campus.getCode();
        this.name = campus.getName();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

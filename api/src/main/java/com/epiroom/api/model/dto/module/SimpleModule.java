package com.epiroom.api.model.dto.module;

import com.epiroom.api.model.Module;

public class SimpleModule {
    private final String code;

    private final String name;

    public SimpleModule(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public SimpleModule(Module module) {
        this.code = module.getCode();
        this.name = "TODO : Retrieve module Name";
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}

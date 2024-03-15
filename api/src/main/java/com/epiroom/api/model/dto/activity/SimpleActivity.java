package com.epiroom.api.model.dto.activity;

import com.epiroom.api.model.Activity;
import com.epiroom.api.model.Module;

public class SimpleActivity {
    private final int id;

    private final String name;

    private final String moduleCode;

    public SimpleActivity(int id, String name, String moduleCode) {
        this.id = id;
        this.name = name;
        this.moduleCode = moduleCode;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public static SimpleActivity from(Activity activity) {
        Module module = activity.getModule();
        return new SimpleActivity(activity.getId(), activity.getTitle(), module == null ? null : module.getCode());
    }
}

package com.epiroom.api.model.dto.activity;

import com.epiroom.api.model.Activity;
import com.epiroom.api.model.Event;
import com.epiroom.api.model.Module;
import com.epiroom.api.model.dto.module.SimpleModule;

import java.util.List;

public class FullActivity {
    private final int id;
    private final String name;
    private final SimpleModule module;
    private final List<Integer> events;

    public FullActivity(int id, String name, Module module, List<Event> events) {
        this.id = id;
        this.name = name;
        this.module = new SimpleModule(module);
        this.events = events.stream().map(Event::getId).toList();
    }

    public FullActivity(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getTitle();
        if (activity.getModule() == null) {
            this.module = null;
        } else {
            this.module = new SimpleModule(activity.getModule());
        }
        this.events = activity.getEvents().stream().map(Event::getId).toList();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public SimpleModule getModule() {
        return module;
    }

    public List<Integer> getEvents() {
        return events;
    }
}

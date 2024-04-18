package com.epiroom.api.model.dto.activity;

import com.epiroom.api.model.Activity;
import com.epiroom.api.model.dto.event.ActivityEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FullActivity {
    private final int id;
    private final String title;
    private final List<ActivityEvent> events;

    public FullActivity(Activity activity) {
        this.id = activity.getId();
        this.title = activity.getTitle();
        this.events = activity.getEvents().stream().map(ActivityEvent::new).toList();
    }
}

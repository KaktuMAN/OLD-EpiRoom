package com.epiroom.api.model.dto.activity;

import com.epiroom.api.model.Activity;

import java.util.List;

public class PaginatedActivity {
    private final int page;
    private final int size;

    private final List<SimpleActivity> activities;

    public PaginatedActivity(int page, List<Activity> activities) {
        this.page = page;
        this.activities = activities.stream().map(SimpleActivity::from).toList();
        this.size = this.activities.size();
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public List<SimpleActivity> getActivities() {
        return activities;
    }
}

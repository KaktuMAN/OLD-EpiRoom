package com.epiroom.api.model.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedActivity {
    public final int page;
    public final int entries;
    public final List<FullActivity> activities;
}

package com.epiroom.api.model.dto.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ActivityInputDTO {
    private final int id;
    private final String title;
    private final String moduleCode;
}

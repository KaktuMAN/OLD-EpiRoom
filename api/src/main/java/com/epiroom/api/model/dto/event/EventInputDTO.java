package com.epiroom.api.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventInputDTO {
    private final int id;
    private final int activityId;
    private final int roomId;
    private final long startTimestamp;
    private final long endTimestamp;
}

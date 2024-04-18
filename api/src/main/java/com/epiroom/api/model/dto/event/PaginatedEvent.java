package com.epiroom.api.model.dto.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PaginatedEvent {
    public final int page;
    public final int entries;
    public final List<FullEvent> events;
}

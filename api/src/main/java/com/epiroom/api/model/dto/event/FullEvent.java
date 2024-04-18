package com.epiroom.api.model.dto.event;

import com.epiroom.api.model.Event;
import com.epiroom.api.model.dto.room.SimpleRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FullEvent {
    private final int id;
    private final String activityName;
    private final long startTimestamp;
    private final long endTimestamp;
    private final SimpleRoom room;

    public FullEvent(Event event) {
        this.id = event.getId();
        this.activityName = event.getActivity().getTitle();
        this.startTimestamp = event.getStart().getTime();
        this.endTimestamp = event.getEnd().getTime();
        this.room = new SimpleRoom(event.getRoom());
    }
}

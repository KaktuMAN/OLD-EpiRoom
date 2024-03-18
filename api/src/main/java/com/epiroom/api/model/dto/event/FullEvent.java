package com.epiroom.api.model.dto.event;

import com.epiroom.api.model.Event;
import com.epiroom.api.model.dto.room.SimpleRoom;

import java.util.Date;

public class FullEvent {
    private final int eventId;

    private final Date start;

    private final Date end;

    private final String activityTitle;

    private final SimpleRoom room;

    public FullEvent(int eventId, Date start, Date end, String activityTitle, SimpleRoom room) {
        this.eventId = eventId;
        this.start = start;
        this.end = end;
        this.activityTitle = activityTitle;
        this.room = room;
    }

    public FullEvent(Event event) {
        this.eventId = event.getId();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.activityTitle = event.getActivity().getTitle();
        this.room = event.getRoomId() == null ? null : new SimpleRoom(event.getRoom());
    }

    public int getEventId() {
        return eventId;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public SimpleRoom getRoom() {
        return room;
    }
}

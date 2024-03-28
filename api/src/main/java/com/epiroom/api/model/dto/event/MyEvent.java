package com.epiroom.api.model.dto.event;

import com.epiroom.api.model.Event;

import java.util.Date;

public class MyEvent {
    private final int eventId;

    private final String title;

    private final String roomName;

    private final String campusCode;

    private final Date start;

    private final Date end;

    public MyEvent(int eventId, String title, String roomName, String campusCode, Date start, Date end) {
        this.eventId = eventId;
        this.title = title;
        this.roomName = roomName;
        this.campusCode = campusCode;
        this.start = start;
        this.end = end;
    }

    public MyEvent(Event event) {
        this.eventId = event.getId();
        this.title = event.getActivity().getTitle();
        if (event.getRoom() == null) {
            this.roomName = null;
            this.campusCode = null;
        } else {
            this.roomName = event.getRoom().getName();
            this.campusCode = event.getRoom().getCampus().getCode();
        }
        this.start = event.getStart();
        this.end = event.getEnd();
    }

    public int getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }
}

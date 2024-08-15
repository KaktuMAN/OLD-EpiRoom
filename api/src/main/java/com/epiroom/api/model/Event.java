package com.epiroom.api.model;

import com.epiroom.api.model.dto.event.EventInputDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "activity_id")
    @NotNull
    private int activityId;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "start_time")
    @NotNull
    private Date start;

    @Column(name = "end_time")
    @NotNull
    private Date end;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    private Activity activity;

    @Setter
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    public Event(EventInputDTO eventInputDTO, Campus campus, Activity activity, Room room) {
        this.id = eventInputDTO.getId();
        this.activityId = eventInputDTO.getActivityId();
        this.roomId = eventInputDTO.getRoomId();
        this.start = new Date(eventInputDTO.getStartTimestamp());
        this.end = new Date(eventInputDTO.getEndTimestamp());
        this.campusCode = campus.getCode();

        this.activity = activity;
        this.room = room;
        this.campus = campus;
    }

    public Event(Event event) {
        this.id = event.getId();
        this.activityId = event.getActivityId();
        this.roomId = event.getRoomId();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.campusCode = event.getCampusCode();

        this.activity = event.getActivity();
        this.room = event.getRoom();
        this.campus = event.getCampus();
    }
}

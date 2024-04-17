package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "events")
public class Event {
    @Id
    @SequenceGenerator(name="events_id_seq", sequenceName="events_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="events_id_seq")
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;
}

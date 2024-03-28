package com.epiroom.api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "activity_id")
    private int activityId;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "start_time")
    private Date start;

    @Column(name = "end_time")
    private Date end;

    @Column(name = "campus_code")
    private String campusCode;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    private Activity activity;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private Room room;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "event_registrations",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "login")
    )
    private List<User> users;

    public Event() {
    }

    public int getId() {
        return id;
    }

    public int getActivityId() {
        return activityId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public Activity getActivity() {
        return activity;
    }

    public Room getRoom() {
        return room;
    }

    public Campus getCampus() {
        return campus;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(String mail) {
        return users.stream().filter(user -> user.getMail().equals(mail)).findFirst().orElse(null);
    }
}

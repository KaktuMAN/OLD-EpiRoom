package com.epiroom.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "activity")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    private String title;

    @Column(name = "module_id")
    private Integer moduleId;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", insertable = false, updatable = false)
    private Module module;

    @JsonBackReference
    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    private List<Event> events;

    public Activity() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Integer getModuleId() {
        return moduleId;
    }

    public Module getModule() {
        return module;
    }

    public List<Event> getEvents() {
        return events;
    }
}

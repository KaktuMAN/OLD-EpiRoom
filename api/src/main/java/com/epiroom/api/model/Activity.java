package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "activities")
public class Activity {
    @Id
    @SequenceGenerator(name="activities_id_seq", sequenceName="activities_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="activities_id_seq")
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "module_id")
    @NotNull
    private Integer moduleId;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id", insertable = false, updatable = false)
    private Module module;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    private List<Event> events;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;
}

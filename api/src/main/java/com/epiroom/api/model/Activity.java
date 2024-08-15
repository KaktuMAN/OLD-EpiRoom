package com.epiroom.api.model;

import com.epiroom.api.model.dto.activity.ActivityInputDTO;
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
    @Column(name = "id", updatable = false)
    private int id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "module_code")
    @NotNull
    private String moduleCode;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_id", insertable = false, updatable = false)
    private List<Event> events;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    public Activity(ActivityInputDTO activityInputDTO, String campusCode) {
        this.id = activityInputDTO.getId();
        this.title = activityInputDTO.getTitle();
        this.moduleCode = activityInputDTO.getModuleCode();
        this.campusCode = campusCode;
    }
}

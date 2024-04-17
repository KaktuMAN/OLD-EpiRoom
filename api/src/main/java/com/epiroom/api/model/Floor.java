package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "floors")
public class Floor {
    @Id
    @SequenceGenerator(name="floors_id_seq", sequenceName="floors_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="floors_id_seq")
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @Column(name = "floor")
    @NotNull
    private int floor;

    @Column(name = "name")
    @NotNull
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", insertable = false, updatable = false)
    private List<Room> rooms;
}
package com.epiroom.api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "rooms")
public class Room {
    public enum RoomType {CLASSROOM, OFFICE, OPENSPACE, MULTIROOM, OTHER}

    @Id
    @SequenceGenerator(name="rooms_id_seq", sequenceName="rooms_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="rooms_id_seq")
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "floor_id")
    @NotNull
    private int floorId;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private RoomType type;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "code")
    @NotNull
    private String code;

    @Column(name = "display_name")
    @NotNull
    private String displayName;

    @Column(name = "seats")
    private Integer seats;

    @Column(name = "display_status")
    @NotNull
    private boolean display_status;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "linked_rooms", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "linked_room_id"))
    private List<Room> linkedRooms;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "floor_id", insertable = false, updatable = false)
    private Floor floor;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;
}
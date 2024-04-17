package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "modules")
public class Module {
    @Id
    @SequenceGenerator(name="modules_id_seq", sequenceName="modules_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="modules_id_seq")
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "code")
    @NotNull
    private String code;

    @Column(name = "year")
    @NotNull
    private int year;

    @Column(name = "semester")
    @NotNull
    private int semester;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;
}

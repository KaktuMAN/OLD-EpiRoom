package com.epiroom.api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "modules")
public class Module {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "code")
    private String code;

    @Column(name = "year")
    private int year;

    @Column(name = "semester")
    private int semester;

    @Column(name = "campus_code")
    private String campusCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    public Module() {
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public int getYear() {
        return year;
    }

    public int getSemester() {
        return semester;
    }

    public String getCampusCode() {
        return campusCode;
    }

    public Campus getCampus() {
        return campus;
    }
}

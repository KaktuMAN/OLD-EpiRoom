package com.epiroom.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "status_messages")
public class StatusMessage {
    @Id
    @SequenceGenerator(name="status_messages_id_seq", sequenceName="status_messages_id_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="status_messages_id_seq")
    @Column(name = "id", updatable=false)
    private int id;

    @Column(name = "campus_code")
    @NotNull
    private String campusCode;

    @Column(name = "creator_mail")
    @NotNull
    private String creatorMail;

    @Column(name = "message")
    @NotNull
    private String message;

    @Column(name = "start_date")
    @NotNull
    private Date startDate;

    @Column(name = "end_date")
    @NotNull
    private Date endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "campus_code", insertable = false, updatable = false)
    private Campus campus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_mail", insertable = false, updatable = false)
    private User creator;
}

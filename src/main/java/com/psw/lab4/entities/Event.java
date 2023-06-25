package com.psw.lab4.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event")
@Data
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "event_name")
    private String eventName;
    @Column(name = "agenda")
    private String agenda;
    @Column(name = "event_date")
    private Date eventDate;

    @ManyToMany(mappedBy = "events")
    private Set<User> users = new HashSet<>();

    public Event(String eventName, String agenda, Date eventDate){
        this.eventName = eventName;
        this.agenda = agenda;
        this.eventDate = eventDate;
    }
}

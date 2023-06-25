package com.psw.lab4.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_event")
@Data
@NoArgsConstructor
public class UserEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnoreProperties("events")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnoreProperties("users")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    private String type;
    private String meals;
    private boolean accepted;

    public UserEvent(User user, Event event, String type, String meals, boolean accepted) {
        this.user = user;
        this.event = event;
        this.type = type;
        this.meals = meals;
        this.accepted = accepted;
    }

    public UserEvent(User user, Event event) {
        this.user = user;
        this.event = event;
    }
}

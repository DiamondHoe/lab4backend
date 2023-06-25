package com.psw.lab4.controllers;

import com.psw.lab4.entities.Event;
import com.psw.lab4.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/add")
    public ResponseEntity<String> addEvent(@RequestBody Event event) {
        eventRepository.save(new Event(event.getEventName(), event.getAgenda(), event.getEventDate()));
        return ResponseEntity.ok("event saved");
    }

    @GetMapping("/all")
    public Iterable<Event> getEventAll() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Long id) {
        Event event = eventRepository.findById(id).orElse(null);

        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateEvent( @RequestBody Event updatedEvent) {
        Event editEvent = eventRepository.findById(updatedEvent.getId()).orElse(null);

        if (editEvent != null) {
            editEvent.setEventName(updatedEvent.getEventName());
            editEvent.setAgenda(updatedEvent.getAgenda());
            editEvent.setEventDate(updatedEvent.getEventDate());
            eventRepository.save(editEvent);

            return ResponseEntity.ok("Event updated");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        Event existingEvent = eventRepository.findById(id).orElse(null);

        if (existingEvent != null) {
            eventRepository.delete(existingEvent);
            return ResponseEntity.ok("Event deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

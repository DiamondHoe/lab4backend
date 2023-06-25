package com.psw.lab4.controllers;

import com.psw.lab4.entities.Event;
import com.psw.lab4.entities.User;
import com.psw.lab4.entities.UserEvent;
import com.psw.lab4.repositories.EventRepository;
import com.psw.lab4.repositories.UserRepository;
import com.psw.lab4.repositories.UserEventRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/registrations")
public class UserEventController {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final UserEventRepository userEventRepository;

    @Autowired
    public UserEventController(UserRepository userRepository, EventRepository eventRepository, UserEventRepository userEventRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.userEventRepository = userEventRepository;
    }

    @GetMapping("/all")
    public Iterable<UserEvent> allRegistrations(){
        return userEventRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUserForEvent(@RequestBody RegistrationRequest request) {

        if((request.eventId == null) || (request.userId == null)){
            return ResponseEntity.badRequest().body("User or event not found");
        }
        User user = userRepository.findById(request.userId).orElse(null);
        Event event = eventRepository.findById(request.eventId).orElse(null);

        if (user == null || event == null) {
            return ResponseEntity.badRequest().body("User or event not found");
        }

        if (userEventRepository.existsByUserAndEvent(user, event)) {
            return ResponseEntity.badRequest().body("User is already registered for the event");
        }
        UserEvent newUserEvent = new UserEvent(user, event, request.type, request.meal, false);
        userEventRepository.save(newUserEvent);

        return ResponseEntity.ok("User registered for event successfully");
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<String> acceptUserEvent(@PathVariable Long id){
        UserEvent existingUserEvent = userEventRepository.findById(id).orElse(null);

        if(existingUserEvent != null){
            existingUserEvent.setAccepted(true);
            userEventRepository.save(existingUserEvent);
            return ResponseEntity.ok("Event accepted");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/decline/{id}")
    public ResponseEntity<String> declineUserEvent(@PathVariable Long id){
        UserEvent existingUserEvent = userEventRepository.findById(id).orElse(null);

        if(existingUserEvent != null){
            existingUserEvent.setAccepted(false);
            userEventRepository.save(existingUserEvent);
            return ResponseEntity.ok("Event accepted");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @Data
    public static class RegistrationRequest {
        private Long userId;
        private Long eventId;
        private String type;
        private String meal;
    }
}

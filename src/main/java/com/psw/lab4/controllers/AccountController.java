package com.psw.lab4.controllers;

import com.psw.lab4.entities.Event;
import com.psw.lab4.entities.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.psw.lab4.repositories.UserRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        try {
            Iterable<User> users = userRepository.findAll();
            boolean isValidUser = StreamSupport.stream(users.spliterator(), false)
                    .anyMatch(user -> username.trim().equals(user.getUsername().trim())
                            && password.trim().equals(user.getPassword().trim()));

            if (isValidUser) {
                User foundUser = StreamSupport.stream(users.spliterator(), false)
                        .filter(user -> username.trim().equals(user.getUsername().trim()))
                        .findFirst()
                        .orElse(null);

                if (foundUser != null) {
                    Map<String, String> response = new HashMap<>();
                    response.put("status", "success");
                    response.put("message", "Login successful");

                    if (foundUser.isPermission()) {
                        response.put("permission", "admin");
                    } else {
                        response.put("permission", "user");
                    }

                    response.put("userId", Long.toString(foundUser.getId()));

                    return ResponseEntity.ok(response);
                }
            }

            return ResponseEntity.badRequest().body(null);
        } catch (Exception exception) {
            System.out.println("User not found.");
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String,String>> register(@RequestBody RegisterRequest request){
        String username = request.getUsername();
        String password = request.getPassword();
        String name = request.getName();
        String lastname = request.getLastname();
        String email = request.getEmail();

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$");
        Matcher matcher = pattern.matcher(email);

        if(username.length() >= 5 &&
                password.length() >= 5 &&
                name.length() >= 3 &&
                lastname.length() >= 3 &&
                matcher.matches())
        {
            userRepository.save(new User(username, password, name, lastname, email, false));
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Register successful");
            return ResponseEntity.ok(response);
        }
        else{
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/users/all")
    public Iterable<User> getUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        User user = userRepository.findById(id).orElse(null);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        // Check if the user exists
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Check if the user is registered for any event
            if (!user.getEvents().isEmpty()) {
                return ResponseEntity.badRequest().body("User cannot be deleted as they are registered for one or more events");
            }

            userRepository.deleteById(id);
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/users/update")
    public ResponseEntity<String> editUser(@RequestBody User user) {
        User editUser = userRepository.findById(user.getId()).orElse(null);

        if(editUser != null){
            editUser.setUsername(user.getUsername());
            editUser.setPassword(user.getPassword());
            editUser.setName(user.getName());
            editUser.setLastname(user.getLastname());
            editUser.setEmail(user.getEmail());
            userRepository.save(editUser);
            return ResponseEntity.ok("Edit successful");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/users/resetpassword/{id}")
    public ResponseEntity<String> resetUserPassword(@PathVariable Long id) {
        User foundUser = userRepository.findById(id).orElse(null);

        if(foundUser != null){
            foundUser.setPassword("Password");
            userRepository.save(foundUser);
            return ResponseEntity.ok("Password reset to 'Password'");
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }
    @Data
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    public static class RegisterRequest {
        private String username;
        private String password;
        private String name;
        private String lastname;
        private String email;
    }
}

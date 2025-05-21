package com.example.smartcourserecommender.controller;

import com.example.smartcourserecommender.model.User;
import com.example.smartcourserecommender.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    // Simple register - no encryption for demo purposes (DO NOT use in production)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        userRepository.save(user);
        return ResponseEntity.ok("Registered");
    }

    // Simple login - no encryption (DO NOT use in production)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> userOpt = userRepository.findByEmail(user.getEmail());
        if (userOpt.isPresent() && userOpt.get().getPassword().equals(user.getPassword())) {
            // Return token as email for simplicity
            return ResponseEntity.ok(new AuthResponse(user.getEmail()));
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    private record AuthResponse(String token) {
    }
}

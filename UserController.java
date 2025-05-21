package com.example.smartcourserecommender.controller;

import com.example.smartcourserecommender.model.Course;
import com.example.smartcourserecommender.model.User;
import com.example.smartcourserecommender.repository.CourseRepository;
import com.example.smartcourserecommender.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    // Update user skills
    @PostMapping("/skills")
    public ResponseEntity<?> updateSkills(@RequestBody Map<String, List<String>> request, @RequestHeader("Authorization") String auth) {
        String email = extractEmailFromAuth(auth);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        User user = userOpt.get();
        List<String> skills = request.get("skills");
        user.setSkills(skills);
        userRepository.save(user);
        return ResponseEntity.ok("Skills updated");
    }

    // Get course recommendations based on user skills (simple matching)
    @GetMapping("/recommendations")
    public ResponseEntity<?> getRecommendations(@RequestHeader("Authorization") String auth) {
        String email = extractEmailFromAuth(auth);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        User user = userOpt.get();

        List<String> userSkills = user.getSkills();
        if (userSkills == null || userSkills.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

        List<Course> courses = courseRepository.findAll();
        List<Course> recommended = courses.stream()
                .filter(c -> c.getTags().stream().anyMatch(userSkills::contains))
                .collect(Collectors.toList());

        return ResponseEntity.ok(recommended);
    }

    // Add a course to favorites
    @PostMapping("/favorites/{courseId}")
    public ResponseEntity<?> addFavorite(@PathVariable Long courseId, @RequestHeader("Authorization") String auth) {
        String email = extractEmailFromAuth(auth);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        User user = userOpt.get();

        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isEmpty()) return ResponseEntity.badRequest().body("Course not found");
        Course course = courseOpt.get();

        List<Course> favorites = user.getFavoriteCourses();
        if (favorites == null) favorites = new ArrayList<>();
        if (!favorites.contains(course)) favorites.add(course);
        user.setFavoriteCourses(favorites);
        userRepository.save(user);
        return ResponseEntity.ok("Added to favorites");
    }

    // Get favorite courses
    @GetMapping("/favorites")
    public ResponseEntity<?> getFavorites(@RequestHeader("Authorization") String auth) {
        String email = extractEmailFromAuth(auth);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.badRequest().body("User not found");
        User user = userOpt.get();

        List<Course> favorites = user.getFavoriteCourses();
        if (favorites == null) favorites = new ArrayList<>();
        return ResponseEntity.ok(favorites);
    }

    // Dummy function to extract email from Authorization header (replace with proper JWT handling)
    private String extractEmailFromAuth(String auth) {
        // Assumes auth = "Bearer user@example.com" for simplicity
        return auth.replace("Bearer ", "").trim();
    }
}

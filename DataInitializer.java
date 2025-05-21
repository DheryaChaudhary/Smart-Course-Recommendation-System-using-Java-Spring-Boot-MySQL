package com.example.smartcourserecommender.config;

import com.example.smartcourserecommender.model.Course;
import com.example.smartcourserecommender.repository.CourseRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class DataInitializer {

    private final CourseRepository courseRepository;

    @PostConstruct
    public void init() {
        if (courseRepository.count() == 0) {
            courseRepository.save(new Course(null, "Java Spring Boot Masterclass", "Udemy",
                    "Learn Spring Boot from scratch",
                    "https://www.udemy.com/course/spring-boot-tutorial/",
                    List.of("java", "spring", "backend")));

            courseRepository.save(new Course(null, "React for Beginners", "Coursera",
                    "Introduction to React.js",
                    "https://www.coursera.org/learn/react",
                    List.of("javascript", "react", "frontend")));

            courseRepository.save(new Course(null, "Python Data Science", "edX",
                    "Learn Data Science using Python",
                    "https://www.edx.org/course/python-data-science",
                    List.of("python", "data-science")));

            courseRepository.save(new Course(null, "Machine Learning A-Z", "Udemy",
                    "Hands-on ML with real-world projects",
                    "https://www.udemy.com/course/machinelearning/",
                    List.of("machine-learning", "python", "data-science")));

            courseRepository.save(new Course(null, "Fullstack Web Development", "freeCodeCamp",
                    "Learn frontend and backend development",
                    "https://www.freecodecamp.org/learn/",
                    List.of("javascript", "frontend", "backend", "react", "nodejs")));
        }
    }
}

package com.example.smartcourserecommender.repository;

import com.example.smartcourserecommender.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}

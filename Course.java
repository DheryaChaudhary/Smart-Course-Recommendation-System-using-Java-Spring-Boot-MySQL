package com.example.smartcourserecommender.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String provider;
    private String description;
    private String url;

    @ElementCollection
    private List<String> tags;

    public Course(Long id, String title, String provider, String description, String url, List<String> tags) {
        this.id = id;
        this.title = title;
        this.provider = provider;
        this.description = description;
        this.url = url;
        this.tags = tags;
    }
}

package com.example.baitapentitymovies.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="actors")
public class Actors {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String slug;
    @Column(columnDefinition = "TEXT")
    private String bio;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.example.baitapentitymovies.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="revies")
public class Reviews {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "Text")
    private String content;
    @Column(columnDefinition = "double default 5.0")
    private double rating;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}

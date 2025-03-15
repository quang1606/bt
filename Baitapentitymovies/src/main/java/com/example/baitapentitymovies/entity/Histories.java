package com.example.baitapentitymovies.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="histories")
public class Histories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String duration;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

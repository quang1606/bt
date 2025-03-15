package com.example.baitapentitymovies.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="favorites")
public class Favorites {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
private LocalDateTime createdAt;
}

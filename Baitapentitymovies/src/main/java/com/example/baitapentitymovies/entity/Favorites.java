package com.example.baitapentitymovies.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="favorites")
public class Favorites {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

private LocalDateTime createdAt;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

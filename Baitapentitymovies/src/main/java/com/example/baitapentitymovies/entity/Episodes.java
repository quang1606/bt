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
@Table(name = "episodes")
public class Episodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int duration;
    @Column(name="display_order")
    private int displayOrder;
    private Boolean status;
    @Column(name = "video_url")
    private String videoUrl;
    @Column(name = "pusblished_at")
    private LocalDateTime publishedAt;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;
}

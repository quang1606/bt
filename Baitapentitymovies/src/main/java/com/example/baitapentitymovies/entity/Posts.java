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
@Table(name = "posts")
public class Posts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String slug;
    @Column(columnDefinition = "TEXT")
    private  String content;
    @Column(columnDefinition =  "TEXT")
    private String description;
    private String thumbnail;
    private Boolean status;
    @Column(name = "create_at")
    private LocalDateTime createdAt;
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
    @Column(name = "pusblished_at")
    private LocalDateTime publishedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

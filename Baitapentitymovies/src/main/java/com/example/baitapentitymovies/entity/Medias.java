package com.example.baitapentitymovies.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "medias")
public class Medias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "user_id")
    private Integer id;
    @Column(name = "media_type")
    private String mediaType;
    private String name;
    private String description;
    private String url;
    private Long size;
    @Column(name="create_at")
    private LocalDateTime createdAt;
}

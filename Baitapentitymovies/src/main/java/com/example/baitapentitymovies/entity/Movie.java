package com.example.baitapentitymovies.entity;

import com.example.baitapentitymovies.model.enums.MovieType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    private String slug;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String thumbnail;
    private Integer releaseYear;
    private boolean status;
    private String trailer;
    @Column(columnDefinition = "double default 5.0")
    private Double rating;
    @Enumerated(EnumType.STRING)
    private MovieType type;
    @Column(name = "ceration_date")
    private LocalDateTime creationDate;
    @Column(name = "update_at")
    private LocalDateTime updateAt;
    @Column(name = "pusblished_at")
    private LocalDateTime publishDate;

}

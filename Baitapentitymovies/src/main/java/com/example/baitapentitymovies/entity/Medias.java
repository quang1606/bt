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
@Table(name = "medias")
public class Medias {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", insertable = false, updatable = false) // Không cho Hibernate insert/update vào user_id
    private Integer id;

    @Column(name = "media_type")
    private String mediaType;

    private String name;
    private String description;
    private String url;
    private Long size;

    @Column(name = "create_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


}

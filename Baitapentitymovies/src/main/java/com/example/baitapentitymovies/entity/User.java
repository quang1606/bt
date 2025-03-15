package com.example.baitapentitymovies.entity;

import com.example.baitapentitymovies.model.enums.Role;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String name;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name ="role", nullable = false,columnDefinition = "varchar(255) default 'USERS'")
    private Role role;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "phone", length = 11,unique = true)
    private String phone;
    private String avatar;
    @Column(name = "is_enabled")
    private boolean isEnabled;
    @Column(name = "create_at")
    private LocalDateTime createdAt;
    @Column(name = "update_at")
    private LocalDateTime updatedAt;
}

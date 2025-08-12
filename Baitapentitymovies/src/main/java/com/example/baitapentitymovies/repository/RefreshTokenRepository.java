package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.RefreshToken;
import com.example.baitapentitymovies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    void deleteByUser(User user);
}
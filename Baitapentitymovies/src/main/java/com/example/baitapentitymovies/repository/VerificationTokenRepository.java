package com.example.baitapentitymovies.repository;

import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.entity.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByUser(User user);

    Optional<VerificationToken> findByToken(String verificationToken);
}
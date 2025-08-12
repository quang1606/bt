package com.example.baitapentitymovies.service;

import com.example.baitapentitymovies.entity.RefreshToken;
import com.example.baitapentitymovies.entity.User;
import com.example.baitapentitymovies.exception.BadRequestException;
import com.example.baitapentitymovies.repository.RefreshTokenRepository;
import com.example.baitapentitymovies.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    @Value("${jwt.refresh-token.expiration}")
    private Long expiration;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    public RefreshToken createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(()->new BadRequestException("User not found"));
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(Instant.now().plusMillis(expiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public void deleteByToken(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(()->new BadRequestException("User not found"));
        refreshTokenRepository.deleteByUser(user);
    }
}

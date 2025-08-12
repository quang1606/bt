package com.example.baitapentitymovies.uitils;

import com.example.baitapentitymovies.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    @Value("${jwt.secret}") // Lấy từ application.properties
    private String secret;
    @Value("${jwt.access-token.expiration}")
    private Long expiration;



    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        return doGenerateToken(claims,userDetails.getUsername(),expiration);
    }

    private String doGenerateToken(Map<String, Object> claims, String username, Long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+expiration))
                .signWith(getSigningKey(),SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String Username = getUsernameFromToken(token);
        return (userDetails.getUsername().equals(Username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Date date = getExpirationDateFromToken(token);
            return date.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token,Claims::getExpiration);
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token,Claims::getSubject);
    }

    private <T> T getClaimFromToken(String token, Function<Claims,T> claimsResolver) {
        final Claims claims = getAllClamsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClamsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] secretBytes = Decoders.BASE64.decode(this.secret);
        return Keys.hmacShaKeyFor(secretBytes);
    }

    public long getRemainingExpiration(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.getTime() - System.currentTimeMillis();
    }
}

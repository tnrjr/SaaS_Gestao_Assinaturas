package com.tary.saas.config;

import com.tary.saas.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String SECRET = "dGhpcy1pcy1hLXNlY3JldC1rZXktdGVzdC1mb3ItanN3dA==";
    private final long EXPIRATION_TIME = 86400000; // 24 horas

    public String generateToken(User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("companyId", user.getCompanyId())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    public Claims extractClaims(String token) throws ExpiredJwtException, MalformedJwtException, SignatureException {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractEmail(String token) {
        return extractClaims(token).getSubject();
    }

    public Long extractCompanyId(String token) {
        return extractClaims(token).get("companyId", Long.class);
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
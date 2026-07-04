package com.inyu.backend.global.security;

import com.inyu.backend.domain.user.entity.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final SecretKey key;
  private final long accessExpiration;
  private final long refreshExpiration;

  public JwtProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-expiration}") long accessExpiration,
      @Value("${jwt.refresh-expiration}") long refreshExpiration
  ) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessExpiration = accessExpiration;
    this.refreshExpiration = refreshExpiration;
  }

  public long getRefreshExpiration() {
    return refreshExpiration;
  }

  public String createAccessToken(String email, UserRole role) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + accessExpiration);

    return Jwts.builder()
        .subject(email)
        .claim("role", role.name())
        .issuedAt(now)
        .expiration(expiry)
        .signWith(key)
        .compact();
  }

  public String createRefreshToken(String email) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + refreshExpiration);

    return Jwts.builder()
        .subject(email)
        .issuedAt(now)
        .expiration(expiry)
        .signWith(key)
        .compact();
  }

  public String getEmail(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
  }

  public String getRole(String token) {
    return Jwts.parser()
        .verifyWith(key)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .get("role", String.class);
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser()
          .verifyWith(key)
          .build()
          .parseSignedClaims(token);
      return true;
    } catch (ExpiredJwtException e) {
      // 만료된 토큰
      return false;
    } catch (JwtException | IllegalArgumentException e) {
      return false;
    }
  }

}

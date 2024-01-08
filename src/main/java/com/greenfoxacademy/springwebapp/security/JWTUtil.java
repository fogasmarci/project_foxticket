package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.models.SecurityUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtil {
  private final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String extractEmail(String token) {
    return extractClaim(token, claims -> claims.get("sub", String.class));
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(SECRET_KEY)
        .build()
        .parseClaimsJws(token)
        .getBody();
    return claims;
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(SecurityUser userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", userDetails.getId());
    claims.put("isAdmin", userDetails.getIsAdmin());
    claims.put("isVerified", userDetails.getIsVerified());

    return createToken(claims, userDetails.getEmail());
  }

  private String createToken(Map<String, Object> claims, String subject) {
    return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(SECRET_KEY, SignatureAlgorithm.HS256).compact();
  }

  public Boolean validateToken(String token, SecurityUser userDetails) {
    final String email = extractEmail(token);
    final boolean isEmailMatch = email.equals(userDetails.getEmail());
    final boolean isTokenValid = !isTokenExpired(token);

    Claims claims = extractAllClaims(token);
    final Integer userId = (Integer) claims.get("userId");
    final boolean isAdmin = (boolean) claims.get("isAdmin");
    final boolean isVerified = (boolean) claims.get("isVerified");

    return isEmailMatch
        && isTokenValid
        && userId == (userDetails.getId())
        && isAdmin == userDetails.getIsAdmin()
        && isVerified == userDetails.getIsVerified();
  }
}

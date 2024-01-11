package com.greenfoxacademy.springwebapp.security;

import com.greenfoxacademy.springwebapp.models.SecurityUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtBuilder {

  private final Key SECRET_KEY;

  public JwtBuilder(@Value("${jwt-secret}") String encodedSecretKey) {
    SECRET_KEY = Keys.hmacShaKeyFor(Base64.getDecoder().decode(encodedSecretKey));
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
}

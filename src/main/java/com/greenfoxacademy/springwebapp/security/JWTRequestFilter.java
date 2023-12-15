package com.greenfoxacademy.springwebapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.models.SecurityUser;
import com.greenfoxacademy.springwebapp.services.JpaUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
  private final ObjectMapper objectMapper;
  private final JpaUserDetailsService userDetailService;
  private final JWTUtil jwtUtil;

  @Autowired
  public JWTRequestFilter(JpaUserDetailsService userDetailService, JWTUtil jwtUtil, ObjectMapper objectMapper) {
    this.userDetailService = userDetailService;
    this.jwtUtil = jwtUtil;
    this.objectMapper = objectMapper;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    final String authorizationHeader = request.getHeader("Authorization");
    String username = null;
    String jwt = null;
    try {
      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        jwt = authorizationHeader.substring(7);
        username = jwtUtil.extractEmail(jwt);
      }

      if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
        SecurityUser userDetails = this.userDetailService.loadUserByUsername(username);
        if (jwtUtil.validateToken(jwt, userDetails)) {
          UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
          usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
      }
      chain.doFilter(request, response);
    } catch (ExpiredJwtException
             | UnsupportedJwtException
             | MalformedJwtException
             | IllegalArgumentException
             | SignatureException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(objectMapper.writeValueAsString(new MessageDTO("Invalid token")));
    }
  }
}
package com.greenfoxacademy.springwebapp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.models.SecurityUser;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.JpaUserDetailsService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.micrometer.common.util.StringUtils;
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
  private final JwtBuilder jwtBuilder;
  private final JwtValidatorService jwtValidatorService;

  @Autowired
  public JWTRequestFilter(JpaUserDetailsService userDetailService, JwtBuilder jwtBuilder, ObjectMapper objectMapper, JwtValidatorService jwtValidatorService) {
    this.jwtBuilder = jwtBuilder;
    this.objectMapper = objectMapper;
    this.jwtValidatorService = jwtValidatorService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    String jwt = getJwtFromHeader(request);

    if (StringUtils.isBlank(jwt)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      SecurityUser userDetails = getUserDetailsFromJwt(jwt);

      setUserAuthenticated(request, userDetails);
      chain.doFilter(request, response);
    } catch (JwtException e) {
      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.getWriter().write(objectMapper.writeValueAsString(new MessageDTO("Invalid token")));
    }
  }

  private SecurityUser getUserDetailsFromJwt(String jwt) {
    Claims claims = jwtValidatorService.parseAndValidateJwtToken(jwt);

    final Long userId = ((Integer) claims.get("userId")).longValue();
    final boolean isAdmin = (boolean) claims.get("isAdmin");
    final boolean isVerified = (boolean) claims.get("isVerified");
    final String email = claims.getSubject();

    User user = new User(userId, email);

    return new SecurityUser(user, isAdmin, isVerified);
  }

  private static String getJwtFromHeader(HttpServletRequest request) {
    final String authorizationHeader = request.getHeader("Authorization");
    String jwt = null;
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      jwt = authorizationHeader.substring(7);
    }
    return jwt;
  }

  private static void setUserAuthenticated(HttpServletRequest request, SecurityUser userDetails) {
    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
  }
}
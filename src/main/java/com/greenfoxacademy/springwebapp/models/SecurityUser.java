package com.greenfoxacademy.springwebapp.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;

public class SecurityUser implements UserDetails {
  private final User user;
  private final boolean isAdmin;
  private final boolean isVerified;

  public SecurityUser(User user, boolean isAdmin, boolean isVerified) {
    this.user = user;
    this.isAdmin = isAdmin;
    this.isVerified = isVerified;
  }

  @Override
  public String getUsername() {
    return user.getName();
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Arrays.stream(user
            .getRoles()
            .split(","))
        .map(role -> new SimpleGrantedAuthority(role.trim()))
        .toList();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public User getUser() {
    return user;
  }

  public String getEmail() {
    return user.getEmail();
  }

  public long getId() {
    return user.getId();
  }

  public boolean getIsAdmin() {
    return isAdmin;
  }

  public boolean getIsVerified() {
    return isVerified;
  }
}

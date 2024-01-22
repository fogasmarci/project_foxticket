package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "role_id")
  private Long roleId;

  @Enumerated(EnumType.STRING)
  private Authorities authority;

  public Role() {
    super();
  }

  public Role(Authorities role) {
    this.authority = role;
  }

  public Long getRoleId() {
    return roleId;
  }

  @Override
  public String getAuthority() {
    return this.authority.toString();
  }

  public void setAuthority(Authorities authority) {
    this.authority = authority;
  }
}

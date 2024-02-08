package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "verification_tokens")
public class VerificationToken {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
  private String token;
  @OneToOne
  private User user;

  public VerificationToken() {
  }

  public VerificationToken(User user) {
    this();
    this.user = user;
  }

  public String getToken() {
    return token;
  }

  public User getUser() {
    return user;
  }
}

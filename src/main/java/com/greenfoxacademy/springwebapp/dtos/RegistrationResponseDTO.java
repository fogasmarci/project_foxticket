package com.greenfoxacademy.springwebapp.dtos;

public class RegistrationResponseDTO {
  private final long id;
  private final String email;
  private final boolean isAdmin;

  public RegistrationResponseDTO(long id, String email, boolean isAdmin) {
    this.id = id;
    this.email = email;
    this.isAdmin = isAdmin;
  }

  public long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }

  public boolean getIsAdmin() {
    return isAdmin;
  }
}
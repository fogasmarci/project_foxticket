package com.greenfoxacademy.springwebapp.dtos;

public class RegistrationResponseDTO {
  private long id;
  private String email;
  private boolean isAdmin;

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

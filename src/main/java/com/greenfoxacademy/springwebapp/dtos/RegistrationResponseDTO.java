package com.greenfoxacademy.springwebapp.dtos;

public class RegistrationResponseDTO {
  private long id;
  private String email;
  private boolean isAdmin;

  public RegistrationResponseDTO() {
  }

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

  public void setId(long id) {
    this.id = id;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setAdmin(boolean admin) {
    isAdmin = admin;
  }
}
package com.greenfoxacademy.springwebapp.dtos;

public class LoginResponseDTO {
  private final String status;
  private final String token;

  public LoginResponseDTO(String token) {
    status = "ok";
    this.token = token;
  }

  public String getStatus() {
    return status;
  }

  public String getToken() {
    return token;
  }
}
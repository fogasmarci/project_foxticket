package com.greenfoxacademy.springwebapp.dtos;

public class LoginUserDTO {
  private String email;
  private String password;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public LoginUserDTO(String email, String password) {
    this.email = email;
    this.password = password;
  }
}

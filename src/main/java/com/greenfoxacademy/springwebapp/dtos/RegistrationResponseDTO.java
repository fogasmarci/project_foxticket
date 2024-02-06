package com.greenfoxacademy.springwebapp.dtos;

public record RegistrationResponseDTO(long id, String email, boolean isAdmin, String message) {
  public RegistrationResponseDTO(long id, String email, boolean isAdmin) {
    this(id, email, isAdmin, "Verification e-mail sent.");
  }
}
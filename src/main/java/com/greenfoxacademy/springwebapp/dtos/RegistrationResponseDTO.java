package com.greenfoxacademy.springwebapp.dtos;

public record RegistrationResponseDTO(long id, String email, boolean isAdmin, String message) {
}
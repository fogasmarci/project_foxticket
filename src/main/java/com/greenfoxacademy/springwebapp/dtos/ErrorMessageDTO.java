package com.greenfoxacademy.springwebapp.dtos;

public class ErrorMessageDTO {
  private final String error;

  public ErrorMessageDTO(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }
}
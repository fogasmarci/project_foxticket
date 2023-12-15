package com.greenfoxacademy.springwebapp.dtos;

public class MessageDTO {
  private final String message;

  public MessageDTO(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}

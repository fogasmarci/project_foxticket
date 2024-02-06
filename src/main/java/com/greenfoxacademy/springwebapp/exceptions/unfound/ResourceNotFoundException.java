package com.greenfoxacademy.springwebapp.exceptions.unfound;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
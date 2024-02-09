package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String message) {
    super(message);
  }
}
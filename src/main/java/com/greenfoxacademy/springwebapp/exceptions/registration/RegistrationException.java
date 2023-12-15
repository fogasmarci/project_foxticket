package com.greenfoxacademy.springwebapp.exceptions.registration;

public class RegistrationException extends RuntimeException {
  public RegistrationException(String message) {
    super(message);
  }
}
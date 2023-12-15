package com.greenfoxacademy.springwebapp.exceptions.registration;

public class ShortPasswordException extends RegistrationException {
  public ShortPasswordException() {
    super("Password must be at least 8 characters.");
  }
}
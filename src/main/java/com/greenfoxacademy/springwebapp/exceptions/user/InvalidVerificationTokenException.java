package com.greenfoxacademy.springwebapp.exceptions.user;

public class InvalidVerificationTokenException extends RuntimeException {
  public InvalidVerificationTokenException() {
    super("Invalid verification token.");
  }
}

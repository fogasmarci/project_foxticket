package com.greenfoxacademy.springwebapp.exceptions.registration;

public class EmailAlreadyTakenException extends RegistrationException {
  public EmailAlreadyTakenException() {
    super("Email is already taken.");
  }
}
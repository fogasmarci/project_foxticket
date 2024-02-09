package com.greenfoxacademy.springwebapp.exceptions.fields;

public class InvalidVerificationTokenException extends FieldsException {
  public InvalidVerificationTokenException() {
    super("Invalid verification token.");
  }
}

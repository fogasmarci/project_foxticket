package com.greenfoxacademy.springwebapp.exceptions.fields;

public class PasswordRequiredException extends MissingFieldsException {
  public PasswordRequiredException() {
    super("Password is required.");
  }
}
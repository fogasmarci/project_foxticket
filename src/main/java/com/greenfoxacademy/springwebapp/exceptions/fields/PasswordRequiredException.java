package com.greenfoxacademy.springwebapp.exceptions.fields;

public class PasswordRequiredException extends FieldsException {
  public PasswordRequiredException() {
    super("Password is required.");
  }
}
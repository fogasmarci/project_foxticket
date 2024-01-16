package com.greenfoxacademy.springwebapp.exceptions.fields;

public class PasswordEmailUpdateException extends FieldsException {
  public PasswordEmailUpdateException() {
    super("Cannot change password and email at the same time.");
  }
}

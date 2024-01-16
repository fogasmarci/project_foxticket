package com.greenfoxacademy.springwebapp.exceptions.fields;

public class InvalidEmailException extends FieldsException {
  public InvalidEmailException() {
    super("Invalid email input.");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.fields;

public class ShortPasswordException extends FieldsException {
  public ShortPasswordException() {
    super("Password must be at least 8 characters.");
  }
}
package com.greenfoxacademy.springwebapp.exceptions.fields;

public class ShortNameException extends FieldsException {
  public ShortNameException() {
    super("Name must be at least 3 letters long");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.fields;

public class AllFieldsMissingException extends FieldsException {
  public AllFieldsMissingException() {
    super("All fields are required.");
  }
}

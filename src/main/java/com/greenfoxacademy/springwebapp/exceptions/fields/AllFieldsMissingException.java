package com.greenfoxacademy.springwebapp.exceptions.fields;

public class AllFieldsMissingException extends MissingFieldsException {
  public AllFieldsMissingException() {
    super("All fields are required.");
  }
}

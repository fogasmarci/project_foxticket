package com.greenfoxacademy.springwebapp.exceptions.fields;

public class EmailRequiredException extends FieldsException {
  public EmailRequiredException() {
    super("Email is required.");
  }
}
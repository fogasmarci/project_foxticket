package com.greenfoxacademy.springwebapp.exceptions.fields;

public class EmailRequiredException extends MissingFieldsException {
  public EmailRequiredException() {
    super("Email is required.");
  }
}
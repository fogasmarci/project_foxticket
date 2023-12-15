package com.greenfoxacademy.springwebapp.exceptions.fields;

public class NameRequiredException extends MissingFieldsException {
  public NameRequiredException() {
    super("Name is required.");
  }
}
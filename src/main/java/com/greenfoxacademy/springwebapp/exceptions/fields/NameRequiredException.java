package com.greenfoxacademy.springwebapp.exceptions.fields;

public class NameRequiredException extends FieldsException {
  public NameRequiredException() {
    super("Name is required.");
  }
}
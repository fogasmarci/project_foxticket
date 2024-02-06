package com.greenfoxacademy.springwebapp.exceptions.fields;

public class DurationIsMalformedException extends FieldsException {

  public DurationIsMalformedException() {
    super("Duration is not valid.");
  }
}

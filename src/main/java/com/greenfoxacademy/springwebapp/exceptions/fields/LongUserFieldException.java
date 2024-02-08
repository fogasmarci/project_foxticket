package com.greenfoxacademy.springwebapp.exceptions.fields;

public class LongUserFieldException extends FieldsException {
  public LongUserFieldException() {
    super("Maximum length is 50.");
  }
}

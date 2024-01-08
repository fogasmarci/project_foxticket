package com.greenfoxacademy.springwebapp.exceptions.fields;

public class MissingFieldsException extends RuntimeException {
  public MissingFieldsException(String message) {
    super(message);
  }
}
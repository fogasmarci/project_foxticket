package com.greenfoxacademy.springwebapp.exceptions.fields;

public class InvalidAmountException extends FieldsException {
  public InvalidAmountException() {
    super("Amount must be greater than 0.");
  }
}

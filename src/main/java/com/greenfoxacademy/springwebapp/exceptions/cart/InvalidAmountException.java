package com.greenfoxacademy.springwebapp.exceptions.cart;

import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;

public class InvalidAmountException extends FieldsException {
  public InvalidAmountException() {
    super("Amount must be greater than 0.");
  }
}

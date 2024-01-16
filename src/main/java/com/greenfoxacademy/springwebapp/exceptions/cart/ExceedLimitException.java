package com.greenfoxacademy.springwebapp.exceptions.cart;

import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;

public class ExceedLimitException extends FieldsException {
  public ExceedLimitException() {
    super("Selected items cannot be added to cart. Cart limit is 50.");
  }
}

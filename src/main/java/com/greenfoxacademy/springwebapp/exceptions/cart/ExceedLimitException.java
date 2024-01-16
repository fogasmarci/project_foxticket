package com.greenfoxacademy.springwebapp.exceptions.cart;

import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;

public class ExceedLimitException extends MissingFieldsException {
  public ExceedLimitException() {
    super("Selected items cannot be added to cart. Cart limit is 50.");
  }
}

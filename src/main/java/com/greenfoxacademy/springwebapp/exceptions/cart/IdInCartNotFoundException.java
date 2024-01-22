package com.greenfoxacademy.springwebapp.exceptions.cart;

import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;

public class IdInCartNotFoundException extends RuntimeException {
  public IdInCartNotFoundException() {
    super("There is no item with the given id in the cart.");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.cart;

public class IdInCartNotFoundException extends RuntimeException {
  public IdInCartNotFoundException() {
    super("There is no item with the given id in the cart.");
  }
}

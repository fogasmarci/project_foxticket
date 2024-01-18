package com.greenfoxacademy.springwebapp.exceptions.cart;

public class CartNotFoundException extends RuntimeException {
  public CartNotFoundException() {
    super("Cart not found");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class CartNotFoundException extends ResourceNotFoundException {
  public CartNotFoundException() {
    super("Cart not found");
  }
}

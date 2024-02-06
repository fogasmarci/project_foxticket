package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class IdInCartNotFoundException extends ResourceNotFoundException {
  public IdInCartNotFoundException() {
    super("There is no item with the given id in the cart.");
  }
}

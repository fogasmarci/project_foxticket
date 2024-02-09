package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class ProductIdInvalidException extends ResourceNotFoundException {
  public ProductIdInvalidException() {
    super("Product doesn't exist.");
  }
}

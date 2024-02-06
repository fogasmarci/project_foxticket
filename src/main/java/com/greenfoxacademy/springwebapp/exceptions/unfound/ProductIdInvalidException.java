package com.greenfoxacademy.springwebapp.exceptions.unfound;

public class ProductIdInvalidException extends ResourceNotFoundException {
  public ProductIdInvalidException() {
    super("Product doesn't exist.");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.product;

public class ProductIdInvalidException extends ProductException {
  public ProductIdInvalidException() {
    super("Product doesn't exist.");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.product;

public class ProductIdMissingException extends ProductException {
  public ProductIdMissingException() {
    super("Product ID is required.");
  }
}

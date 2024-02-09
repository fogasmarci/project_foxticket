package com.greenfoxacademy.springwebapp.exceptions.fields;

public class ProductIdRequiredException extends FieldsException {
  public ProductIdRequiredException() {
    super("Product ID is required.");
  }
}

package com.greenfoxacademy.springwebapp.exceptions.product;

public class ProductNameAlreadyTakenException extends ProductException {
  public ProductNameAlreadyTakenException() {
    super("Product name already exists.");
  }
}
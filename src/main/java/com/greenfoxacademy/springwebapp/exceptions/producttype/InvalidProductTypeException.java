package com.greenfoxacademy.springwebapp.exceptions.producttype;

public class InvalidProductTypeException extends ProductTypeException {
  public InvalidProductTypeException() {
    super("Product type is wrong.");
  }
}
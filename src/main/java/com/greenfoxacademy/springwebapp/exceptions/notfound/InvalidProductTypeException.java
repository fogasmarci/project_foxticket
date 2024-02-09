package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class InvalidProductTypeException extends ResourceNotFoundException {
  public InvalidProductTypeException() {
    super("Product type is wrong.");
  }
}
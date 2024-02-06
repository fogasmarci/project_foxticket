package com.greenfoxacademy.springwebapp.exceptions.unfound;

public class InvalidProductTypeException extends ResourceNotFoundException {
  public InvalidProductTypeException() {
    super("Product type is wrong.");
  }
}
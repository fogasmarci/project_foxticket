package com.greenfoxacademy.springwebapp.exceptions.taken;

public class ProductNameAlreadyTakenException extends AlreadyTakenException {
  public ProductNameAlreadyTakenException() {
    super("Product name already exists.");
  }
}
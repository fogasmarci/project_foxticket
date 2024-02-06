package com.greenfoxacademy.springwebapp.exceptions.taken;

public class ProductTypeNameAlreadyExist extends AlreadyTakenException {
  public ProductTypeNameAlreadyExist() {
    super("Product type name already exists");
  }
}

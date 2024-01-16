package com.greenfoxacademy.springwebapp.exceptions.producttype;

public class ProductTypeNameAlreadyExist extends ProductTypeException {
  public ProductTypeNameAlreadyExist() {
    super("Product type name already exists");
  }
}

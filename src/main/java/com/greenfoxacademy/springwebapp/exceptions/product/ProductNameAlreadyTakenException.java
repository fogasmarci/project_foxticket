package com.greenfoxacademy.springwebapp.exceptions.product;

import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;

public class ProductNameAlreadyTakenException extends RegistrationException {
  public ProductNameAlreadyTakenException() {
    super("Product name already exists.");
  }
}
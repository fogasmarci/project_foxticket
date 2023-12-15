package com.greenfoxacademy.springwebapp.exceptions.producttype;

import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;

public class InvalidProductTypeException extends RegistrationException {
  public InvalidProductTypeException() {
    super("Product type is wrong.");
  }
}
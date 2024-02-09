package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class NotMyOrderException extends ResourceNotFoundException {
  public NotMyOrderException() {
    super("This order does not belong to the user.");
  }
}

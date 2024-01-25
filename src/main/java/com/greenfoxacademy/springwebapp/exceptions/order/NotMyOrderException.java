package com.greenfoxacademy.springwebapp.exceptions.order;

public class NotMyOrderException extends RuntimeException {
  public NotMyOrderException() {
    super("This order does not belong to the user.");
  }
}

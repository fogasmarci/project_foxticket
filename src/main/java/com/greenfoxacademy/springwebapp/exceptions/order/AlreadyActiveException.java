package com.greenfoxacademy.springwebapp.exceptions.order;

public class AlreadyActiveException extends RuntimeException {
  public AlreadyActiveException() {
    super("This item is already active.");
  }
}

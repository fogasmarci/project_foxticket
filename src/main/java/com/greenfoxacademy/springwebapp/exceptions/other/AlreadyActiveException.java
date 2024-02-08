package com.greenfoxacademy.springwebapp.exceptions.other;

public class AlreadyActiveException extends RuntimeException {
  public AlreadyActiveException() {
    super("This item is already active.");
  }
}

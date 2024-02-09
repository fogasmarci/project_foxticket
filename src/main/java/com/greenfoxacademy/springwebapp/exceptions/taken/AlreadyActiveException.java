package com.greenfoxacademy.springwebapp.exceptions.taken;

public class AlreadyActiveException extends AlreadyTakenException {
  public AlreadyActiveException() {
    super("This item is already active.");
  }
}

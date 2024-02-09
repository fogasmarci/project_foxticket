package com.greenfoxacademy.springwebapp.exceptions.taken;

public class AlreadyTakenException extends RuntimeException {
  public AlreadyTakenException(String message) {
    super(message);
  }
}

package com.greenfoxacademy.springwebapp.exceptions.user;

public class InvalidUserIdException extends RuntimeException {
  public InvalidUserIdException() {
    super("User id doesn't exist.");
  }
}

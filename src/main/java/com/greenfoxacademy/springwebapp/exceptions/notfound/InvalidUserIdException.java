package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class InvalidUserIdException extends ResourceNotFoundException {
  public InvalidUserIdException() {
    super("User id doesn't exist.");
  }
}

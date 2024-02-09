package com.greenfoxacademy.springwebapp.exceptions.unauthorized;

public class IncorrectCredentialsException extends RuntimeException {
  public IncorrectCredentialsException(Throwable throwable) {
    super("Email or password is incorrect.", throwable);
  }
}

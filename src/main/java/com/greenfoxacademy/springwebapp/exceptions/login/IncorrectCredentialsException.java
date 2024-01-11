package com.greenfoxacademy.springwebapp.exceptions.login;

public class IncorrectCredentialsException extends LoginException {
  public IncorrectCredentialsException(Throwable throwable) {
    super("Email or password is incorrect.", throwable);
  }
}

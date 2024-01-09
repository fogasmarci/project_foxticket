package com.greenfoxacademy.springwebapp.exceptions.login;

public class IncorrectCredentialsException extends LoginException {
  public IncorrectCredentialsException() {
    super("Email or password is incorrect.");
  }
}

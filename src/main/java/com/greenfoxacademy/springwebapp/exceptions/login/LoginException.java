package com.greenfoxacademy.springwebapp.exceptions.login;

public class LoginException extends RuntimeException {
  public LoginException(String message, Throwable throwable) {
    super(message, throwable);
  }
}

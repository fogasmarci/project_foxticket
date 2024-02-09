package com.greenfoxacademy.springwebapp.exceptions.server;

public class FailedToSendEmailException extends RuntimeException {

  public FailedToSendEmailException() {
    super("An error occurred while trying to send the verification e-mail.");
  }
}

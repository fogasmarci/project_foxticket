package com.greenfoxacademy.springwebapp.exceptions.verificationemail;

public class FailedToSendEmailException extends RuntimeException {

  public FailedToSendEmailException() {
    super("An error occurred while trying to send the verification e-mail.");
  }
}

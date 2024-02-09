package com.greenfoxacademy.springwebapp.exceptions.taken;

public class EmailAlreadyTakenException extends AlreadyTakenException {
  public EmailAlreadyTakenException() {
    super("Email is already taken.");
  }
}
package com.greenfoxacademy.springwebapp.exceptions.durationconverter;

public class DurationIsMalformedException extends RuntimeException {

  public DurationIsMalformedException() {
    super("Duration is not valid.");
  }
}

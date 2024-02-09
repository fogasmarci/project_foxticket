package com.greenfoxacademy.springwebapp.exceptions.files;

public class MaxUploadSizeException extends RuntimeException {
  public MaxUploadSizeException() {
    super("Uploaded picture sized must be less than 1MB.");
  }
}

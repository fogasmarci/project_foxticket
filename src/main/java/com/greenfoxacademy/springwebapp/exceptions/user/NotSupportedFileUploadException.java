package com.greenfoxacademy.springwebapp.exceptions.user;

public class NotSupportedFileUploadException extends RuntimeException {
  public NotSupportedFileUploadException() {
    super("Uploaded images must adhere to the file formats .jpg, .jpeg, or .png.");
  }
}

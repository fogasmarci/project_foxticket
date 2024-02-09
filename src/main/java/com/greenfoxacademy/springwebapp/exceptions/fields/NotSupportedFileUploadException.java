package com.greenfoxacademy.springwebapp.exceptions.fields;

public class NotSupportedFileUploadException extends FieldsException {
  public NotSupportedFileUploadException() {
    super("Uploaded images must adhere to the file formats .jpg, .jpeg, or .png.");
  }
}

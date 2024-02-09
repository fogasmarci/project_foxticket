package com.greenfoxacademy.springwebapp.exceptions.fields;

public class ArticleTitleRequiredException extends FieldsException {
  public ArticleTitleRequiredException() {
    super("Title is required");
  }
}

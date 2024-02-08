package com.greenfoxacademy.springwebapp.exceptions.fields;

public class ArticleContentRequiredException extends FieldsException {
  public ArticleContentRequiredException() {
    super("Content is required");
  }
}

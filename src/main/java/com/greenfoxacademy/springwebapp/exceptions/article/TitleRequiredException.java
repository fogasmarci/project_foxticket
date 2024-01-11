package com.greenfoxacademy.springwebapp.exceptions.article;

public class TitleRequiredException extends ArticleException {
  public TitleRequiredException() {
    super("Title is required");
  }
}

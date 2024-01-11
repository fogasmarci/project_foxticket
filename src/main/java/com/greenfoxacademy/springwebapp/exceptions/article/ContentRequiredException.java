package com.greenfoxacademy.springwebapp.exceptions.article;

public class ContentRequiredException extends ArticleException {
  public ContentRequiredException() {
    super("Content is required");
  }
}

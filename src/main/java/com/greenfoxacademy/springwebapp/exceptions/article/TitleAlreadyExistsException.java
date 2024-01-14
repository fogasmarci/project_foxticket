package com.greenfoxacademy.springwebapp.exceptions.article;

public class TitleAlreadyExistsException extends ArticleException {
  public TitleAlreadyExistsException() {
    super("News title already exists");
  }
}

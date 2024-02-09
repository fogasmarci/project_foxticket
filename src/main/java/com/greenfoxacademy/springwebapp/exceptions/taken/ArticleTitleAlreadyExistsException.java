package com.greenfoxacademy.springwebapp.exceptions.taken;

public class ArticleTitleAlreadyExistsException extends AlreadyTakenException {
  public ArticleTitleAlreadyExistsException() {
    super("News title already exists");
  }
}

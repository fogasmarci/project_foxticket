package com.greenfoxacademy.springwebapp.exceptions.article;

public class ArticleNotExistsException extends ArticleException {
  public ArticleNotExistsException() {
    super("Article not exists by this ID");
  }
}

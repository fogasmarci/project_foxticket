package com.greenfoxacademy.springwebapp.exceptions.notfound;

public class ArticleIdNotFoundException extends ResourceNotFoundException {
  public ArticleIdNotFoundException() {
    super("Article does not exist by this ID");
  }
}

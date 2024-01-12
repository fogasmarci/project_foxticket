package com.greenfoxacademy.springwebapp.dtos;

public class AddArticleDTO {
  private final String title;
  private final String content;


  public AddArticleDTO(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }
}

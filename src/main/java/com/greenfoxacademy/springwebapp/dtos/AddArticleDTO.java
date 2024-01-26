package com.greenfoxacademy.springwebapp.dtos;

public class AddArticleDTO {
  private String title;
  private String content;

  public AddArticleDTO() {
  }

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

  public void setTitle(String title) {
    this.title = title;
  }

  public void setContent(String content) {
    this.content = content;
  }
}

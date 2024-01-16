package com.greenfoxacademy.springwebapp.dtos;

public class NameDTO {
  private String name;

  public NameDTO(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

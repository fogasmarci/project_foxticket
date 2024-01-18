package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class NameDTO {
  private String name;
  @JsonIgnore
  private String name2;

  public NameDTO(String name) {
    name2 = "a";
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}

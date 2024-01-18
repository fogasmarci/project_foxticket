package com.greenfoxacademy.springwebapp.dtos;

public class ProductTypeResponseDTO {
  private Long id;
  private String name;

  public ProductTypeResponseDTO(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setId(Long id) {
    this.id = id;
  }
}

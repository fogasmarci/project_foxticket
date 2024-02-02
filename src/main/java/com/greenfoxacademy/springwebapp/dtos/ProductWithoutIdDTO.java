package com.greenfoxacademy.springwebapp.dtos;

public record ProductWithoutIdDTO(String name, Integer price, String durationInString,
                                  String description, Long typeId) {

  public ProductWithoutIdDTO(String name, Integer price, String durationInString, String description, Long typeId) {
    this.name = name;
    this.price = price;
    this.durationInString = durationInString;
    this.description = description;
    this.typeId = typeId;
  }

  @Override
  public Long typeId() {
    return typeId;
  }

  @Override
  public String durationInString() {
    return durationInString;
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public Integer price() {
    return price;
  }

  @Override
  public String description() {
    return description;
  }
}
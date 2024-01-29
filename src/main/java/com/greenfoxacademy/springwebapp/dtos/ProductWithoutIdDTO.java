package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductWithoutIdDTO {
  private final String name;
  private final Integer price;
  private final Integer duration;
  private final String description;
  @JsonProperty("item_id")
  private Long typeId;

  public ProductWithoutIdDTO(String name, Integer price, Integer duration, String description, Long typeId) {
    this.name = name;
    this.price = price;
    this.duration = duration;
    this.description = description;
    this.typeId = typeId;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public Integer getDuration() {
    return duration;
  }

  public String getDescription() {
    return description;
  }

  public Long getTypeId() {
    return typeId;
  }
}
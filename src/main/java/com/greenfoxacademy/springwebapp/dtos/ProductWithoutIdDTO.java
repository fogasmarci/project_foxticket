package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class ProductWithoutIdDTO {
  private final String name;
  private final Integer price;
  private final Duration duration;
  private final String description;
  @JsonProperty("item_id")
  private final Long typeId;

  public ProductWithoutIdDTO(String name, Integer price, Duration duration, String description, Long typeId) {
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

  public Duration getDuration() {
    return duration;
  }

  public String getDescription() {
    return description;
  }

  public Long getTypeId() {
    return typeId;
  }
}
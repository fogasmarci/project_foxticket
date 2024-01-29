package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public class ProductWithoutIdDTO {
  private String name;
  private Integer price;
  private Duration duration;
  private String description;
  @JsonProperty("item_id")
  private Long typeId;

  public ProductWithoutIdDTO() {
  }

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

  public void setName(String name) {
    this.name = name;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public void setDuration(Duration duration) {
    this.duration = duration;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setTypeId(Long typeId) {
    this.typeId = typeId;
  }
}
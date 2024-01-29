package com.greenfoxacademy.springwebapp.dtos;

import java.time.Duration;

public class ProductDTO {
  private Long id;
  private String name;
  private int price;
  private Duration duration;
  private String description;
  private String type;

  public ProductDTO(Long id, String name, int price, Duration duration, String description, String type) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.duration = duration;
    this.description = description;
    this.type = type;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public Duration getDuration() {
    return duration;
  }

  public String getDescription() {
    return description;
  }

  public String getType() {
    return type;
  }
}
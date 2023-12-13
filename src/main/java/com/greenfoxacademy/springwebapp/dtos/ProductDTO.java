package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.ProductType;
import jakarta.persistence.ManyToOne;

public class ProductDTO {
  private Long id;
  private String name;
  private int price;
  private int duration;
  private String description;
  private String type;

  public ProductDTO() {}

  public ProductDTO(String name, int price, int duration, String description, String type) {
    this();
    this.name = name;
    this.price = price;
    this.duration = duration;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getType() {
    return type;
  }

  public void String(String type) {
    this.type = type;
  }
}


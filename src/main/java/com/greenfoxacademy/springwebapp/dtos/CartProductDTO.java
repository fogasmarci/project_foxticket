package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CartProductDTO {
  @JsonProperty("product_id")
  private Long productId;
  private String name;
  private int price;

  public CartProductDTO(Long productId, String name, int price) {
    this.productId = productId;
    this.name = name;
    this.price = price;
  }

  public Long getProductId() {
    return productId;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }
}

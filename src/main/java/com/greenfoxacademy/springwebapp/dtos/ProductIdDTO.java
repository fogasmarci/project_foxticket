package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductIdDTO {
  private Long productId;
  private int amount;

  public ProductIdDTO(@JsonProperty("productId") Long productId) {
    this.productId = productId;
    amount = 1;
  }

  public ProductIdDTO(@JsonProperty("productId") Long productId, int amount) {
    this.productId = productId;
    this.amount = amount;
  }

  public Long getProductId() {
    return productId;
  }

  public int getAmount() {
    return amount;
  }
}

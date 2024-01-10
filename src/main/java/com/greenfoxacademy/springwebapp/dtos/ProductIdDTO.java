package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductIdDTO {
  private Long productId;

  public ProductIdDTO(@JsonProperty("productId") Long productId) {
    this.productId = productId;
  }

  public Long getProductId() {
    return productId;
  }
}

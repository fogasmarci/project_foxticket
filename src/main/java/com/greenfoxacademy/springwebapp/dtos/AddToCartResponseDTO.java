package com.greenfoxacademy.springwebapp.dtos;

public class AddToCartResponseDTO {
  private Long id;
  private Long productId;

  public AddToCartResponseDTO(Long id, Long productId) {
    this.id = id;
    this.productId = productId;
  }

  public Long getId() {
    return id;
  }

  public Long getProductId() {
    return productId;
  }
}

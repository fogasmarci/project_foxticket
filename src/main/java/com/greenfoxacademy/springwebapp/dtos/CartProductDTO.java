package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.springwebapp.models.Product;

public class CartProductDTO {
  @JsonProperty("product_id")
  private Long productId;
  private String name;
  private int price;
  private int quantity;

  public CartProductDTO(Long productId, String name, int price, int quantity) {
    this.productId = productId;
    this.name = name;
    this.price = price;
    this.quantity = quantity;
  }

  public CartProductDTO(Product product, int quantity) {
    productId = product.getId();
    name = product.getName();
    price = product.getPrice();
    this.quantity = quantity;
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

  public int getQuantity() {
    return quantity;
  }
}

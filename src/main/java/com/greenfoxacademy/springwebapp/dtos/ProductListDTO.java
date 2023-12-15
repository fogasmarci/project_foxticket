package com.greenfoxacademy.springwebapp.dtos;

import java.util.List;

public class ProductListDTO {
  private List<ProductDTO> products;

  public ProductListDTO(List<ProductDTO> products) {
    this.products = products;
  }

  public List<ProductDTO> getProducts() {
    return products;
  }
}

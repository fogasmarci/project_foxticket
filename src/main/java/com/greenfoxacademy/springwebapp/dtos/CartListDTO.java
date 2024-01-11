package com.greenfoxacademy.springwebapp.dtos;

import java.util.List;

public class CartListDTO {
  private List<CartProductDTO> cart;

  public CartListDTO(List<CartProductDTO> cart) {
    this.cart = cart;
  }

  public List<CartProductDTO> getCart() {
    return cart;
  }
}

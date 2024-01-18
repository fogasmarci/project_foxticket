package com.greenfoxacademy.springwebapp.dtos;

import java.util.List;

public class OrderListDTO {
  private List<OrderedItemDTO> orders;

  public OrderListDTO(List<OrderedItemDTO> orders) {
    this.orders = orders;
  }

  public List<OrderedItemDTO> getOrders() {
    return orders;
  }
}

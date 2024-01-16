package com.greenfoxacademy.springwebapp.dtos;

import java.util.List;

public class OrderListDTO {
  private List<OrderDTO> orders;

  public OrderListDTO(List<OrderDTO> orders) {
    this.orders = orders;
  }

  public List<OrderDTO> getOrders() {
    return orders;
  }
}

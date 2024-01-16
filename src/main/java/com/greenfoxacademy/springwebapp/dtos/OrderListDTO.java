package com.greenfoxacademy.springwebapp.dtos;

import com.greenfoxacademy.springwebapp.models.Order;

import java.util.List;

public class OrderListDTO {
  private List<Order> orders;

  public OrderListDTO(List<Order> orders) {
    this.orders = orders;
  }

  public List<Order> getOrders() {
    return orders;
  }
}

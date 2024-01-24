package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {
  private final OrderService orderService;

  @Autowired
  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @GetMapping("/api/orders")
  public ResponseEntity<OrderListDTO> listAllPurchasedItems() {
    return ResponseEntity.status(200).body(orderService.listAllPurchases());
  }

  @PostMapping("/api/orders/{orderId}")
  public ResponseEntity<OrderedItemDTO> activatePurchasedItem(Long orderId) {
    return ResponseEntity.status(200).body(orderService.activateItemPurchased(orderId));
  }
}

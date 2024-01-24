package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;

public interface OrderService {
  OrderListDTO listAllPurchases();

  OrderedItemDTO activateItemPurchased(Long orderId);
}

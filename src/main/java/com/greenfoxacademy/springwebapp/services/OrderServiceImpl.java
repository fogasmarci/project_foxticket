package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
  private final UserService userService;
  private final CartService cartService;

  @Autowired
  public OrderServiceImpl(UserService userService, CartService cartService) {
    this.userService = userService;
    this.cartService = cartService;
  }

  @Override
  public OrderListDTO listAllPurchases() {
    User user = userService.getCurrentUser();
    List<OrderedItemDTO> purchases = cartService.mapOrdersIntoListOfOrderDTOs(user.getOrders());
    return new OrderListDTO(purchases);
  }

  @Override
  public OrderedItemDTO activateItemPurchased(Long orderId) {
    User user = userService.getCurrentUser();
    List<OrderedItemDTO> purchases = cartService.mapOrdersIntoListOfOrderDTOs(user.getOrders());
    return null;
  }
}

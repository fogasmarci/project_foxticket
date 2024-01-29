package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final UserService userService;

  @Autowired
  public OrderServiceImpl(OrderRepository orderRepository, UserService userService) {
    this.orderRepository = orderRepository;
    this.userService = userService;
  }

  @Override
  public OrderListDTO listAllPurchases() {
    User user = userService.getCurrentUser();
    List<OrderedItemDTO> purchases = mapOrdersIntoListOfOrderDTOs(user.getOrders());
    return new OrderListDTO(purchases);
  }

  @Override
  public OrderedItem saveOrder(OrderedItem orderedItem) {
    return orderRepository.save(orderedItem);
  }

  @Override
  public List<OrderedItemDTO> mapOrdersIntoListOfOrderDTOs(List<OrderedItem> orderedItems) {
    return orderedItems.stream()
        .map(OrderedItemDTO::new)
        .toList();
  }
}

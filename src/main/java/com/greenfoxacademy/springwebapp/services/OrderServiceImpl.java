package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static com.greenfoxacademy.springwebapp.models.Status.Active;

@Service
public class OrderServiceImpl implements OrderService {
  private final UserService userService;
  private final CartService cartService;
  private final OrderRepository orderRepository;

  @Autowired
  public OrderServiceImpl(UserService userService, CartService cartService, OrderRepository orderRepository) {
    this.userService = userService;
    this.cartService = cartService;
    this.orderRepository = orderRepository;
  }

  @Override
  public OrderListDTO listAllPurchases() {
    User user = userService.getCurrentUser();
    List<OrderedItemDTO> purchases = cartService.mapOrdersIntoListOfOrderDTOs(user.getOrders());
    return new OrderListDTO(purchases);
  }

  @Override
  public OrderedItemDTO activateItem(Long orderId) {
    User user = userService.getCurrentUser();
    List<OrderedItem> orderedItems = user.getOrders();

    OrderedItem orderedItemToActivate = orderedItems.stream()
        .filter(orderedItem -> orderedItem.getId().equals(orderId))
        .findFirst()
        .orElseThrow(NotMyOrderException::new);

    Duration duration = orderedItemToActivate.getProduct().getDuration();
    orderedItemToActivate.setExpiry(LocalDateTime.now().plus(duration));
    orderedItemToActivate.setStatus(Active);
    orderRepository.save(orderedItemToActivate);

    return new OrderedItemDTO(orderedItemToActivate);
  }
}

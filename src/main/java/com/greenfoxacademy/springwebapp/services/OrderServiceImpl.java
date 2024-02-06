package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.exceptions.order.AlreadyActiveException;
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

  @Override
  public OrderedItemDTO activateItem(Long orderId) {
    User user = userService.getCurrentUser();
    List<OrderedItem> orderedItems = user.getOrders();

    OrderedItem orderedItemToActivate = orderedItems.stream()
        .filter(orderedItem -> orderedItem.getId().equals(orderId))
        .findFirst()
        .orElseThrow(NotMyOrderException::new);

    if (orderedItemToActivate.getStatus().equals(Active)) {
      throw new AlreadyActiveException();
    }

    Duration duration = orderedItemToActivate.getProduct().getDuration();
    orderedItemToActivate.setExpiry(LocalDateTime.now().plus(duration));
    orderedItemToActivate.setStatus(Active);
    orderRepository.save(orderedItemToActivate);

    return new OrderedItemDTO(orderedItemToActivate);
  }
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.InvalidProductTypeException;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
  @Transactional
  public OrderedItemDTO activateItem(Long orderId) {
    User user = userService.getCurrentUser();
    List<OrderedItem> orderedItems = user.getOrders();

    OrderedItem orderedItemToActivate = orderedItems.stream()
        .filter(orderedItem -> orderedItem.getId().equals(orderId))
        .findFirst()
        .orElseThrow(NotMyOrderException::new);

    orderedItemToActivate.setStatus(Active);
    adjustExpiryDate(orderedItemToActivate);
    orderRepository.save(orderedItemToActivate);

    return new OrderedItemDTO(orderedItemToActivate);
  }

  private void adjustExpiryDate(OrderedItem orderedItemToActivate){
    String getTypeName = orderedItemToActivate.getProduct().getType().getName();
    switch (getTypeName) {
      case "bérlet":
        orderedItemToActivate.setExpiry(LocalDateTime.now().plusDays(30));
        break;
      case "jegy":
        orderedItemToActivate.setExpiry(LocalDateTime.now());
        break;
      default:
        throw new InvalidProductTypeException();
    }
  }
}

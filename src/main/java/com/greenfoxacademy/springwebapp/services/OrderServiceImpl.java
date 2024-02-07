package com.greenfoxacademy.springwebapp.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;
import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.exceptions.order.AlreadyActiveException;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.models.DurationConverter;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    OrderedItem orderedItemToActivate = user.getOrders().stream()
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

  @Override
  public byte[] getQrCode(Long orderId) throws IOException, WriterException {
    User user = userService.getCurrentUser();
    OrderedItem orderedItem = user.getOrders().stream()
        .filter(item -> orderId.equals(item.getId()))
        .findFirst()
        .orElseThrow(NotMyOrderException::new);

    OrderQrCodeDTO json = mapEntitiesToQrCodeDto(user, orderedItem);

    ObjectMapper objectMapper = new ObjectMapper();
    String jsonAsString = objectMapper.writeValueAsString(json);

    return QrCodeGenerator.createQrCode(jsonAsString, 250, "png");
  }

  private OrderQrCodeDTO mapEntitiesToQrCodeDto(User user, OrderedItem item) {
    NameEmailDTO userDTO = new NameEmailDTO(user.getName(), user.getEmail());

    Product product = item.getProduct();
    String name = product.getName();
    String duration = DurationConverter.convertDurationtoString(product.getDuration());
    String typeName = product.getType().getName();
    String status = item.getStatus().toString();
    String expiryDate = item.getExpiry() == null ? "Not yet activated" : item.getExpiry().toString();

    ProductQrCodeDTO productDTO = new ProductQrCodeDTO(name, duration, typeName, status, expiryDate);

    return new OrderQrCodeDTO(userDTO, productDTO);
  }
}

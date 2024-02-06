package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.exceptions.order.AlreadyActiveException;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.models.*;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import com.greenfoxacademy.springwebapp.services.OrderServiceImpl;
import com.greenfoxacademy.springwebapp.services.UserService;
import com.greenfoxacademy.springwebapp.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.models.Status.Active;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
public class OrderServiceTest {
  OrderRepository orderRepository;
  UserService userService;
  OrderServiceImpl orderService;

  public OrderServiceTest() {
    orderRepository = Mockito.mock(OrderRepository.class);
    userService = Mockito.mock(UserServiceImpl.class);
    orderService = new OrderServiceImpl(orderRepository, userService);
  }

  @Test
  void listAllPurchases_WithNoItemsPurchased_ReturnsEmptyOrdersList() {
    User user = new User();
    List<OrderedItemDTO> emptyList = new ArrayList<>();

    Mockito.when(userService.getCurrentUser()).thenReturn(user);

    assertThat(orderService.listAllPurchases()).usingRecursiveComparison().isEqualTo(new OrderListDTO(emptyList));
  }

  @Test
  void listAllPurchases_With4ItemsPurchased_ReturnsCorrectOrdersList() {
    User user = new User();
    Product productMock1 = Mockito.mock(Product.class);
    Mockito.when(productMock1.getId()).thenReturn(1L);
    OrderedItem orderedItem1 = new OrderedItem();
    orderedItem1.setProduct(productMock1);
    user.addOrder(orderedItem1);

    Product productMock2 = Mockito.mock(Product.class);
    Mockito.when(productMock2.getId()).thenReturn(2L);
    OrderedItem orderedItem2 = new OrderedItem();
    orderedItem2.setProduct(productMock2);
    user.addOrder(orderedItem2);

    List<OrderedItemDTO> orderList = new ArrayList<>();
    orderList.add(new OrderedItemDTO(orderedItem1));
    orderList.add(new OrderedItemDTO(orderedItem2));

    Mockito.when(userService.getCurrentUser()).thenReturn(user);

    assertThat(orderService.listAllPurchases()).usingRecursiveComparison().isEqualTo(new OrderListDTO(orderList));
  }

  @Test
  void activateItem_WithCorrectOrderId_WorksCorrectly() {
    Long orderId = 1L;
    User user = new User();
    OrderedItem orderedItem = new OrderedItem();
    orderedItem.setId(1L);

    Product product = new Product("vonaljegy", 100, Duration.ofHours(1), "String description");
    ProductType productType = new ProductType("jegy");
    product.setType(productType);

    orderedItem.setProduct(product);
    user.addOrder(orderedItem);

    Mockito.when(userService.getCurrentUser()).thenReturn(user);
    Mockito.when(orderRepository.save(Mockito.any(OrderedItem.class))).thenReturn(orderedItem);

    OrderedItemDTO orderedItemToActivate = orderService.activateItem(orderId);
    Status status = orderedItemToActivate.getStatus();
    assertEquals(status, Active);
  }

  @Test
  void activateItem_WithInvalidOrderId_ThrowsCorrectException() {
    Long orderId = 2L;
    User user = new User();
    OrderedItem orderedItem = new OrderedItem();
    orderedItem.setId(1L);

    user.addOrder(orderedItem);
    Mockito.when(userService.getCurrentUser()).thenReturn(user);

    assertThrows(NotMyOrderException.class, () -> orderService.activateItem(orderId));
  }

  @Test
  void activateItem_ItemAlreadyActive_ThrowsCorrectException() {
    Long orderId = 1L;
    User user = new User();

    OrderedItem orderedItem = new OrderedItem();
    orderedItem.setId(1L);
    orderedItem.setStatus(Active);
    user.addOrder(orderedItem);

    Mockito.when(userService.getCurrentUser()).thenReturn(user);

    Throwable exception = assertThrows(AlreadyActiveException.class, () -> orderService.activateItem(orderId));
    assertEquals("This item is already active.", exception.getMessage());
  }
}

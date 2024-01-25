package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.exceptions.order.NotMyOrderException;
import com.greenfoxacademy.springwebapp.models.*;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import com.greenfoxacademy.springwebapp.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static com.greenfoxacademy.springwebapp.models.Status.Active;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
public class OrderServiceTest {
  UserService userService;
  CartService cartService;
  OrderRepository orderRepository;
  OrderServiceImpl orderService;

  public OrderServiceTest() {
    userService = Mockito.mock(UserServiceImpl.class);
    cartService = Mockito.mock(CartServiceImpl.class);
    orderRepository = Mockito.mock(OrderRepository.class);
    orderService = new OrderServiceImpl(userService, cartService, orderRepository);
  }

  @Test
  void listAllPurchases_WithNoItemsPurchased_ReturnsEmptyOrdersList() {
    User user = new User();
    List<OrderedItemDTO> emptyList = new ArrayList<>();

    Mockito.when(userService.getCurrentUser()).thenReturn(user);
    Mockito.when(cartService.mapOrdersIntoListOfOrderDTOs(user.getOrders())).thenReturn(emptyList);

    assertThat(orderService.listAllPurchases()).usingRecursiveComparison().isEqualTo(new OrderListDTO(emptyList));
  }

  @Test
  void listAllPurchases_With4ItemsPurchased_ReturnsCorrectOrdersList() {
    User user = new User();
    OrderedItemDTO item1 = new OrderedItemDTO(1L, Status.Not_active, null, 1L);
    OrderedItemDTO item2 = new OrderedItemDTO(2L, Status.Not_active, null, 2L);
    OrderedItemDTO item3 = new OrderedItemDTO(3L, Status.Not_active, null, 2L);
    OrderedItemDTO item4 = new OrderedItemDTO(4L, Status.Not_active, null, 3L);
    List<OrderedItemDTO> orderList = new ArrayList<>(List.of(item1, item2, item3, item4));

    Mockito.when(userService.getCurrentUser()).thenReturn(user);
    Mockito.when(cartService.mapOrdersIntoListOfOrderDTOs(user.getOrders())).thenReturn(orderList);

    assertThat(orderService.listAllPurchases()).usingRecursiveComparison().isEqualTo(new OrderListDTO(orderList));
  }

  @Test
  void activateItem_WithCorrectOrderId_WorksCorrectly() {
    Long orderId = 1L;
    User user = new User();
    OrderedItem orderedItem = new OrderedItem();
    orderedItem.setId(1L);

    Product product = new Product();
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
}

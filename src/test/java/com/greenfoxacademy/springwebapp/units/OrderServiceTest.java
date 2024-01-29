package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderedItemDTO;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import com.greenfoxacademy.springwebapp.services.OrderServiceImpl;
import com.greenfoxacademy.springwebapp.services.UserService;
import com.greenfoxacademy.springwebapp.services.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.User;

import java.util.List;

public interface CartService {
  void putProductsInCart(ProductIdDTO productIdDTO);

  Cart findCartByUser(User user);

  CartListDTO getCartWithProducts();

  OrderListDTO buyProductsInCart();

  CartListDTO createPutProductsInCartResponse();

  MessageDTO removeProductFromCart(Long itemId);

  MessageDTO removeAllProductsFromCart();

  List<OrderedItemDTO> mapOrdersIntoListOfOrderDTOs(List<OrderedItem> orderedItems);
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;

public interface CartService {
  void putProductsInCart(ProductIdDTO productIdDTO);

  Cart findCartByUser(User user);

  CartListDTO getCartWithProducts();

  OrderListDTO buyProductsInCart();

  CartListDTO createPutProductsInCartResponse();

  MessageDTO removeProductFromCart(Long itemId);

  MessageDTO removeAllProductsFromCart();
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;

public interface CartService {
  void putProductsInCart(Cart cart, ProductIdDTO productIdDTO);

  Cart findCartByUser(User user);

  CartListDTO getCartWithProducts(Long userId);

  OrderListDTO buyProductsInCart();

  CartListDTO createPutProductsInCartResponse(Long userId);

  MessageDTO removeProductFromCart(Long itemId);

  MessageDTO removeAllProductsFromCart();
}

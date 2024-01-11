package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;

public interface CartService {
  void addProductToCart(Cart cart, Long productId);

  Cart findCartByUser(User user);

  CartListDTO findCartWithProductsByUser(Long userId);
}

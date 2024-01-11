package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;

import java.util.List;

public interface CartService {
  void addProductToCart(Cart cart, Long productId);

  Cart findCartByUser(User user);

  List<Product> findProductsInCart(Cart cart);

  CartListDTO createCartListDTO(List<Product> productsInCart);
}

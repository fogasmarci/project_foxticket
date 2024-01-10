package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.AddToCartResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartController {
  private CartService cartService;

  @Autowired
  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @RequestMapping(path = "/api/cart", method = RequestMethod.POST)
  public ResponseEntity<?> addProductToCart(@RequestBody ProductIdDTO productIdDTO) {
    User user = cartService.findLoggedInUser();
    Cart cart = cartService.findCartByUser(user);

    try {
      Long productId = productIdDTO.getProductId();
      cartService.addProductToCart(cart, productId);
      return ResponseEntity.status(200).body(new AddToCartResponseDTO(cart.getId(), productId));
    } catch (ProductException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

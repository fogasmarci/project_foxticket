package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
  private final CartService cartService;

  @Autowired
  public CartController(CartService cartService) {
    this.cartService = cartService;
  }

  @PostMapping("/api/cart")
  public ResponseEntity<CartListDTO> addProductToCart(@RequestBody ProductIdDTO productIdDTO) {
    cartService.putProductsInCart(productIdDTO);
    CartListDTO productsInUsersCart = cartService.createPutProductsInCartResponse();
    return ResponseEntity.status(200).body(productsInUsersCart);
  }

  @GetMapping("/api/cart")
  public ResponseEntity<CartListDTO> listCartContents() {
    CartListDTO productsInUsersCart = cartService.getCartWithProducts();
    return ResponseEntity.status(200).body(productsInUsersCart);
  }

  @PostMapping("/api/orders")
  public ResponseEntity<OrderListDTO> buyProductsInCart() {
    OrderListDTO orderListDTO = cartService.buyProductsInCart();
    return ResponseEntity.status(200).body(orderListDTO);
  }

  @DeleteMapping("/api/cart/{id}")
  public ResponseEntity<MessageDTO> removeItemFromCart(@PathVariable Long id) {
    return ResponseEntity.status(200).body(cartService.removeProductFromCart(id));
  }

  @DeleteMapping("/api/cart")
  public ResponseEntity<MessageDTO> removeAllItemsFromCart() {
    return ResponseEntity.status(200).body(cartService.removeAllProductsFromCart());
  }
}

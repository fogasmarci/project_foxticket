package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.OrderListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.cart.CartNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.cart.IdInCartNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.CartService;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
public class CartController {
  private CartService cartService;
  private UserService userService;

  @Autowired
  public CartController(CartService cartService, UserService userService) {
    this.cartService = cartService;
    this.userService = userService;
  }

  @PostMapping("/api/cart")
  public ResponseEntity<?> addProductToCart(@RequestBody ProductIdDTO productIdDTO) {
    User user = userService.findLoggedInUser();
    Cart cart = cartService.findCartByUser(user);

    try {
      cartService.putProductsInCart(cart, productIdDTO);
      CartListDTO productsInUsersCart = cartService.createPutProductsInCartResponse(user.getId());
      return ResponseEntity.status(200).body(productsInUsersCart);
    } catch (ProductException | FieldsException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @GetMapping("/api/cart")
  public ResponseEntity<CartListDTO> listCartContents() {
    Long userId = userService.findLoggedInUsersId();
    CartListDTO productsInUsersCart = cartService.getCartWithProducts(userId);
    return ResponseEntity.status(200).body(productsInUsersCart);
  }

  @PostMapping("/api/orders")
  public ResponseEntity<?> buyProductsInCart() {
    try {
      OrderListDTO orderListDTO = cartService.buyProductsInCart();
      return ResponseEntity.status(200).body(orderListDTO);
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @DeleteMapping("/api/cart/{id}")
  public ResponseEntity<?> removeItemFromCart(@PathVariable Long id) {
    try {
      return ResponseEntity.status(200).body(cartService.removeProductFromCart(id));
    } catch (CartNotFoundException | ProductIdInvalidException | IdInCartNotFoundException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @DeleteMapping("/api/cart")
  public ResponseEntity<?> removeAllItemsFromCart() {
    try {
      return ResponseEntity.status(200).body(cartService.removeAllProductsFromCart());
    } catch (CartNotFoundException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdMissingException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.services.CartServiceImpl;
import com.greenfoxacademy.springwebapp.services.ProductService;
import com.greenfoxacademy.springwebapp.services.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
public class CartServiceTest {
  CartServiceImpl cartService;
  CartRepository cartRepository;
  ProductService productService;

  public CartServiceTest() {
    cartRepository = Mockito.mock(CartRepository.class);
    productService = Mockito.mock(ProductServiceImpl.class);
    cartService = new CartServiceImpl(cartRepository, productService);
  }

  @Test
  void addProductToCart_WithNullProductId_ThrowsCorrectException() {
    Cart cart = new Cart();
    Throwable exception = assertThrows(ProductIdMissingException.class, () -> cartService.addProductToCart(cart, null));
    assertEquals("Product ID is required.", exception.getMessage());
  }

  @Test
  void addProductToCart_WithInvalidProductId_ThrowsCorrectException() {
    Cart cart = new Cart();
    Mockito.when(productService.findProductById(50L)).thenReturn(Optional.empty());
    Throwable exception = assertThrows(ProductIdInvalidException.class, () -> cartService.addProductToCart(cart, 50L));
    assertEquals("Product doesn't exist.", exception.getMessage());
  }

  @Test
  void addProductToCart_WithValidProductId_WorksCorrectly() {
    Product product = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));
    Cart cart = new Cart();

    cartService.addProductToCart(cart, 2L);
    verify(cartRepository, times(1)).save(cart);
  }

  @Test
  void findCartByUser_ReturnsCartOfUser() {
    User user = new User();
    Cart cart = new Cart();
    cart.setUser(user);
    Mockito.when(cartRepository.findByUser(user)).thenReturn(user.getCart());
    assertThat(cartService.findCartByUser(user)).usingRecursiveComparison().isEqualTo(cart);
  }
}

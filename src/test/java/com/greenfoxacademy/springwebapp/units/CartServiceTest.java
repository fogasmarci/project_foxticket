package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.cart.ExceedLimitException;
import com.greenfoxacademy.springwebapp.exceptions.cart.InvalidAmountException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdMissingException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import com.greenfoxacademy.springwebapp.services.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class CartServiceTest {
  CartServiceImpl cartService;
  CartRepository cartRepository;
  ProductService productService;
  UserService userService;
  OrderRepository orderRepository;

  public CartServiceTest() {
    cartRepository = Mockito.mock(CartRepository.class);
    productService = Mockito.mock(ProductServiceImpl.class);
    userService = Mockito.mock(UserServiceImpl.class);
    orderRepository = Mockito.mock(OrderRepository.class);
    cartService = new CartServiceImpl(cartRepository, productService, userService, orderRepository);
  }

  @Test
  void putProductsInCart_WithNullProductId_ThrowsCorrectException() {
    Cart cart = new Cart();
    ProductIdDTO productIdDTO = new ProductIdDTO(null);
    Throwable exception = assertThrows(ProductIdMissingException.class, () -> cartService.putProductsInCart(cart, productIdDTO));
    assertEquals("Product ID is required.", exception.getMessage());
  }

  @Test
  void putProductsInCart_WithInvalidProductId_ThrowsCorrectException() {
    Cart cart = new Cart();
    ProductIdDTO productIdDTO = new ProductIdDTO(50L);
    Mockito.when(productService.findProductById(50L)).thenReturn(Optional.empty());
    Throwable exception = assertThrows(ProductIdInvalidException.class, () -> cartService.putProductsInCart(cart, productIdDTO));
    assertEquals("Product doesn't exist.", exception.getMessage());
  }

  @Test
  void putProductsInCart_WithValidProductId_WorksCorrectly() {
    Product product = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductIdDTO productIdDTO = new ProductIdDTO(2L);
    Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));
    Cart cart = new Cart();

    cartService.putProductsInCart(cart, productIdDTO);
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

  @Test
  void getCartWithProducts_WithLoggedInUserId_ReturnsCorrectCartContent() {
    User user = new User();
    Cart cart = user.getCart();

    ProductType type1 = new ProductType("bérlet");
    ProductType type2 = new ProductType("jegy");

    Product product1 = new Product("teszt bérlet 1", 10000, 9000, "havi teljes aru berlet");
    product1.setType(type1);
    Product product2 = new Product("teszt bérlet 2", 4000, 9000, "havi diakberlet");
    product2.setType(type1);
    Product product3 = new Product("teszt vonaljegy", 400, 90, "egyszer hasznalhato");
    product3.setType(type2);

    cart.putProductInCart(product1, 3);
    cart.putProductInCart(product1, 13);
    cart.putProductInCart(product2, 1);
    cart.putProductInCart(product3, 5);

    Mockito.when(cartRepository.findOne(any(Specification.class))).thenReturn(Optional.of(cart));

    List<CartProductDTO> cartContent = cart.getProductsInCart().keySet().stream()
        .map(p -> new CartProductDTO(p, cart.getProductsInCart().get(p)))
        .toList();
    CartListDTO cartListDTO = new CartListDTO(cartContent);

    assertThat(cartService.getCartWithProducts(user.getId())).usingRecursiveComparison().isEqualTo(cartListDTO);
  }

  @Test
  void putProductsInCart_WithValidProductId_AndAmount_WorksCorrectly() {
    Product product = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, 3);
    Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));
    Cart cart = new Cart();

    cartService.putProductsInCart(cart, productIdDTO);
    verify(cartRepository, times(1)).save(cart);
  }

  @Test
  void putProductsInCart_WithValidProductId_AndNegativeAmount_ThrowsCorrectException() {
    Product product = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, -5);
    Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));
    Cart cart = new Cart();

    Throwable exception = assertThrows(InvalidAmountException.class, () -> cartService.putProductsInCart(cart, productIdDTO));
    assertEquals("Amount must be greater than 0.", exception.getMessage());
  }

  @Test
  void putProductsInCart_WithValidProductId_AndZeroAmount_ThrowsCorrectException() {
    Product product = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, 0);
    Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));
    Cart cart = new Cart();

    Throwable exception = assertThrows(InvalidAmountException.class, () -> cartService.putProductsInCart(cart, productIdDTO));
    assertEquals("Amount must be greater than 0.", exception.getMessage());
  }

  @Test
  void putProductsInCart_WithNullProductId_AndValidAmount_ThrowsCorrectException() {
    Cart cart = new Cart();
    ProductIdDTO productIdDTO = new ProductIdDTO(null, 3);
    Throwable exception = assertThrows(ProductIdMissingException.class, () -> cartService.putProductsInCart(cart, productIdDTO));
    assertEquals("Product ID is required.", exception.getMessage());
  }

  @Test
  void putProductsInCart_WithInvalidProductId_AndValidAmount_ThrowsCorrectException() {
    Cart cart = new Cart();
    ProductIdDTO productIdDTO = new ProductIdDTO(50L, 3);
    Mockito.when(productService.findProductById(50L)).thenReturn(Optional.empty());
    Throwable exception = assertThrows(ProductIdInvalidException.class, () -> cartService.putProductsInCart(cart, productIdDTO));
    assertEquals("Product doesn't exist.", exception.getMessage());
  }

  @Test
  void putProductsInCart_WithValidProductId_AndAmountOverLimit_ThrowsException() {
    Product product = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, 52);
    Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));
    Cart cart = new Cart();

    assertThrows(ExceedLimitException.class, () -> cartService.putProductsInCart(cart, productIdDTO),
        "Selected items cannot be added to cart. Cart limit is 50.");
  }
}

package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.CartListDTO;
import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.cart.ExceedLimitException;
import com.greenfoxacademy.springwebapp.exceptions.cart.IdInCartNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.cart.InvalidAmountException;
import com.greenfoxacademy.springwebapp.exceptions.fields.ProductIdRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.unfound.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.services.*;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.List;
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
  UserService userService;
  OrderService orderService;
  Cart cart;

  @BeforeEach
  public void cartServiceTests() {
    cartRepository = Mockito.mock(CartRepository.class);
    productService = Mockito.mock(ProductServiceImpl.class);
    userService = Mockito.mock(UserServiceImpl.class);
    orderService = Mockito.mock(OrderServiceImpl.class);
    cartService = new CartServiceImpl(cartRepository, productService, userService, orderService);
  }

  @Nested
  class EmptyCartTests {
    @BeforeEach
    void createEmptyCart() {
      cart = returnEmptyCart();
    }

    @Test
    void putProductsInCart_WithNullProductId_ThrowsCorrectException() {
      ProductIdDTO productIdDTO = new ProductIdDTO(null);

      Throwable exception = assertThrows(ProductIdRequiredException.class, () -> cartService.putProductsInCart(productIdDTO));
      assertEquals("Product ID is required.", exception.getMessage());
    }

    @Test
    void putProductsInCart_WithInvalidProductId_ThrowsCorrectException() {
      ProductIdDTO productIdDTO = new ProductIdDTO(50L);
      Mockito.when(productService.findProductById(50L)).thenReturn(Optional.empty());

      Throwable exception = assertThrows(ProductIdInvalidException.class, () -> cartService.putProductsInCart(productIdDTO));
      assertEquals("Product doesn't exist.", exception.getMessage());
    }

    @Test
    void putProductsInCart_WithValidProductId_WorksCorrectly() {
      Product product = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
      ProductIdDTO productIdDTO = new ProductIdDTO(2L);
      Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));

      cartService.putProductsInCart(productIdDTO);

      verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void putProductsInCart_WithValidProductId_AndAmount_WorksCorrectly() {
      Product product = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
      ProductIdDTO productIdDTO = new ProductIdDTO(2L, 3);
      Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));

      cartService.putProductsInCart(productIdDTO);

      verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void putProductsInCart_WithValidProductId_AndNegativeAmount_ThrowsCorrectException() {
      Product product = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
      ProductIdDTO productIdDTO = new ProductIdDTO(2L, -5);
      Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));

      Throwable exception = assertThrows(InvalidAmountException.class, () -> cartService.putProductsInCart(productIdDTO));
      assertEquals("Amount must be greater than 0.", exception.getMessage());
    }

    @Test
    void putProductsInCart_WithValidProductId_AndZeroAmount_ThrowsCorrectException() {
      Product product = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
      ProductIdDTO productIdDTO = new ProductIdDTO(2L, 0);
      Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));

      Throwable exception = assertThrows(InvalidAmountException.class, () -> cartService.putProductsInCart(productIdDTO));
      assertEquals("Amount must be greater than 0.", exception.getMessage());
    }

    @Test
    void putProductsInCart_WithNullProductId_AndValidAmount_ThrowsCorrectException() {
      ProductIdDTO productIdDTO = new ProductIdDTO(null, 3);

      Throwable exception = assertThrows(ProductIdRequiredException.class, () -> cartService.putProductsInCart(productIdDTO));
      assertEquals("Product ID is required.", exception.getMessage());
    }

    @Test
    void putProductsInCart_WithInvalidProductId_AndValidAmount_ThrowsCorrectException() {
      ProductIdDTO productIdDTO = new ProductIdDTO(50L, 3);
      Mockito.when(productService.findProductById(50L)).thenReturn(Optional.empty());

      Throwable exception = assertThrows(ProductIdInvalidException.class, () -> cartService.putProductsInCart(productIdDTO));
      assertEquals("Product doesn't exist.", exception.getMessage());
    }

    @Test
    void putProductsInCart_WithValidProductId_AndAmountOverLimit_ThrowsException() {
      Product product = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
      ProductIdDTO productIdDTO = new ProductIdDTO(2L, 52);
      Mockito.when(productService.findProductById(2L)).thenReturn(Optional.of(product));

      assertThrows(ExceedLimitException.class, () -> cartService.putProductsInCart(productIdDTO),
          "Selected items cannot be added to cart. Cart limit is 50.");
    }
  }

  @Nested
  class NotEmptyCartTests {
    @BeforeEach
    void createCartWithProducts() {
      cart = returnCartWithProducts();
    }

    @Test
    void getCartWithProducts_WithLoggedInUserId_ReturnsCorrectCartContent() {
      List<CartProductDTO> cartContent = cart.getProductsInCart().keySet().stream()
          .map(p -> new CartProductDTO(p, cart.getProductsInCart().get(p)))
          .toList();
      CartListDTO cartListDTO = new CartListDTO(cartContent);

      assertThat(cartService.getCartWithProducts()).usingRecursiveComparison().isEqualTo(cartListDTO);
    }

    @Test
    void removeProductFromCart_ItemIsSuccessfullyRemoved() {
      Product itemToRemove = new Product("teszt bérlet 1", 10000, Duration.ofDays(30), "havi teljes aru berlet");
      Mockito.when(productService.findProductById(itemToRemove.getId())).thenReturn(Optional.of(itemToRemove));
      Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(null);

      MessageDTO messageDTO = cartService.removeProductFromCart(itemToRemove.getId());

      assertEquals("Teszt bérlet 1 is deleted from the cart.", messageDTO.message());
    }

    @Test
    void removeProductFromCart_ItemIdInCartNotFound_ThrowsCorrectException() {
      Product itemToRemove = new Product("teszt bérlet 111", 10000, Duration.ofDays(30), "havi teljes aru berlet");
      Mockito.when(productService.findProductById(itemToRemove.getId())).thenReturn(Optional.of(itemToRemove));

      assertThrows(IdInCartNotFoundException.class, () -> cartService.removeProductFromCart(itemToRemove.getId()),
          "There is no item with the given id in the cart.");
    }

    @Test
    void removeProductFromCart_WithInvalidProductId_ThrowsCorrectException() {
      Long productId = 11L;
      Mockito.when(productService.findProductById(productId)).thenThrow(ProductIdInvalidException.class);

      assertThrows(ProductIdInvalidException.class, () -> cartService.removeProductFromCart(productId),
          "Product doesn't exist.");
    }

    @Test
    void removeAllProductsFromCart_ItemsAreSuccessfullyRemoved() {
      cart.getProductsInCart().clear();
      Mockito.when(cartRepository.save(Mockito.any(Cart.class))).thenReturn(cart);

      MessageDTO messageDTO = cartService.removeAllProductsFromCart();

      assertEquals("All items are cleared from the cart.", messageDTO.message());
      Assert.isTrue(cart.getProductsInCart().keySet().isEmpty());
    }
  }

  @Test
  void findCartByUser_ReturnsCartOfUser() {
    User user = new User();
    Cart cart = new Cart();
    cart.setUser(user);
    Mockito.when(cartRepository.findByUser(user)).thenReturn(user.getCart());
    assertThat(cartService.findCartByUser(user)).usingRecursiveComparison().isEqualTo(cart);
  }

  private Cart returnEmptyCart() {
    User user = new User();
    Long userId = user.getId();
    Cart cart = user.getCart();

    Mockito.when(userService.findLoggedInUsersId()).thenReturn(userId);
    Mockito.when(cartRepository.findOne(Mockito.<Specification<Cart>>any())).thenReturn(Optional.of(cart));
    return cart;
  }

  private Cart returnCartWithProducts() {
    Cart cart = returnEmptyCart();

    ProductType type1 = new ProductType("bérlet");
    ProductType type2 = new ProductType("jegy");
    Product product1 = new Product("teszt bérlet 1", 10000, Duration.ofDays(30), "havi teljes aru berlet");
    Product product2 = new Product("teszt bérlet 2", 4000, Duration.ofDays(30), "havi diakberlet");
    Product product3 = new Product("teszt vonaljegy", 400, Duration.ofHours(1), "egyszer hasznalhato");

    product1.setType(type1);
    product2.setType(type1);
    product3.setType(type2);

    cart.putProductInCart(product1, 3);
    cart.putProductInCart(product2, 1);
    cart.putProductInCart(product3, 5);

    return cart;
  }
}
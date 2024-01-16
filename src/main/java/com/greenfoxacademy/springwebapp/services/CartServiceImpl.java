package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.exceptions.cart.InvalidAmountException;
import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdMissingException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.Order;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.greenfoxacademy.springwebapp.models.CartSpecifications.hasUserId;

@Service
public class CartServiceImpl implements CartService {
  private CartRepository cartRepository;
  private ProductService productService;
  private UserService userService;
  private OrderRepository orderRepository;

  @Autowired
  public CartServiceImpl(CartRepository cartRepository, ProductService productService, UserService userService, OrderRepository orderRepository) {
    this.cartRepository = cartRepository;
    this.productService = productService;
    this.userService = userService;
    this.orderRepository = orderRepository;
  }

  @Override
  @Transactional
  public void addProductToCart(Cart cart, ProductIdDTO productIdDTO) {
    Long productId = productIdDTO.getProductId();
    if (productId == null) {
      throw new ProductIdMissingException();
    }

    int amount = productIdDTO.getAmount();
    if (amount <= 0) {
      throw new InvalidAmountException();
    }

    Optional<Product> optionalProduct = productService.findProductById(productId);
    if (optionalProduct.isPresent()) {
      Product productToAdd = optionalProduct.get();
      for (int i = 0; i < amount; i++) {
        cart.addProduct(productToAdd);
      }

      cartRepository.save(cart);

    } else {
      throw new ProductIdInvalidException();
    }
  }

  @Override
  public Cart findCartByUser(User user) {
    return cartRepository.findByUser(user);
  }

  public CartListDTO getCartWithProducts(Long userId) {
    Specification<Cart> specification = hasUserId(userId);
    List<Cart> carts = cartRepository.findAll(specification);

    List<CartProductDTO> productsInCart = carts.stream()
        .flatMap(cart -> cart.getProducts().stream()
            .map(CartProductDTO::new))
        .toList();

    return new CartListDTO(productsInCart);
  }

  @Override
  @Transactional
  public OrderListDTO buyProductsInCart() {
    User user = userService.getCurrentUser();
    Specification<Cart> specification = hasUserId(user.getId());
    List<Cart> carts = cartRepository.findAll(specification);

    List<Order> orders = carts.stream()
        .flatMap(cart -> cart.getProducts().stream()
            .map(p -> {
              Order o = new Order();
              o.setProduct(p);
              o.setUser(user);
              orderRepository.save(o);
              return o;
            }))
        .toList();

    List<OrderDTO> orderDTOList = orders.stream()
        .map(o -> new OrderDTO(o.getId(), o.getStatus(), o.getExpiry(), o.getProduct().getId()))
        .toList();

    return new OrderListDTO(orderDTOList);
  }
}
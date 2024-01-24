package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.exceptions.cart.CartNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.cart.InvalidAmountException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdMissingException;
import com.greenfoxacademy.springwebapp.models.Cart;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.CartRepository;
import com.greenfoxacademy.springwebapp.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
  public Cart findCartByUser(User user) {
    return cartRepository.findByUser(user);
  }

  @Override
  public CartListDTO getCartWithProducts() {
    Cart cart = getCart();
    List<CartProductDTO> productsInUsersCart = mapCartContentToList(cart);

    return new CartListDTO(productsInUsersCart);
  }

  @Override
  @Transactional
  public void putProductsInCart(ProductIdDTO productIdDTO) {
    Cart cart = getCart();
    Long productId = productIdDTO.getProductId();
    if (productId == null) {
      throw new ProductIdMissingException();
    }

    int amount = productIdDTO.getAmount();
    if (amount <= 0) {
      throw new InvalidAmountException();
    }

    Product productToAdd = productService.findProductById(productId).orElseThrow(ProductIdInvalidException::new);
    cart.putProductInCart(productToAdd, amount);
    cartRepository.save(cart);
  }

  @Override
  public CartListDTO createPutProductsInCartResponse() {
    Cart cart = getCart();
    List<CartProductDTO> productsInCart = mapCartContentToList(cart);

    return new CartListDTO(productsInCart);
  }

  @Override
  @Transactional
  public OrderListDTO buyProductsInCart() {
    User user = userService.getCurrentUser();
    Cart cart = user.getCart();
    List<OrderedItem> orderedItems = new ArrayList<>();

    for (Map.Entry<Product, Integer> e : cart.getProductsInCart().entrySet()) {
      for (int i = 0; i < e.getValue(); i++) {
        OrderedItem o = new OrderedItem();
        o.setProduct(e.getKey());
        o.setUser(user);
        orderRepository.save(o);
        orderedItems.add(o);
      }
    }

    cart.clear();
    cartRepository.save(cart);

    return new OrderListDTO(mapOrdersIntoListOfOrderDTOs(orderedItems));
  }

  public MessageDTO removeProductFromCart(Long itemId) {
    Cart cart = getCart();
    Product product = productService.findProductById(itemId)
        .orElseThrow(ProductIdInvalidException::new);

    cart.removeProduct(product);
    cartRepository.save(cart);

    String productName = product.getName().substring(0, 1).toUpperCase() + product.getName().substring(1);
    String okMessage = String.format("%s is deleted from the cart.", productName);
    return new MessageDTO(okMessage);
  }

  public MessageDTO removeAllProductsFromCart() {
    Cart cart = getCart();

    cart.clearProducts();
    cartRepository.save(cart);

    String okMessage = "All items are cleared from the cart.";
    return new MessageDTO(okMessage);
  }

  private Cart getCart() {
    Specification<Cart> specification = hasUserId(userService.findLoggedInUsersId());
    return cartRepository.findOne(specification).orElseThrow(CartNotFoundException::new);
  }

  private List<OrderedItemDTO> mapOrdersIntoListOfOrderDTOs(List<OrderedItem> orderedItems) {
    return orderedItems.stream()
        .map(o -> new OrderedItemDTO(o.getId(), o.getStatus(), o.getExpiry(), o.getProduct().getId()))
        .toList();
  }

  private List<CartProductDTO> mapCartContentToList(Cart cart) {
    return cart.getProductsInCart().keySet().stream()
        .map(p -> new CartProductDTO(p, cart.getProductsInCart().get(p)))
        .toList();
  }
}
package com.greenfoxacademy.springwebapp.models;

import com.greenfoxacademy.springwebapp.exceptions.cart.ExceedLimitException;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @OneToOne(mappedBy = "cart")
  private User user;
  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "cart_products", joinColumns = @JoinColumn(name = "cart_id"))
  @MapKeyJoinColumn(name = "product_id")
  @Column(name = "quantity")
  private Map<Product, Integer> productsInCart;
  private int quantity;
  static final int productLimit = 50;

  public Cart() {
    productsInCart = new HashMap<>();
  }

  public Long getId() {
    return id;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public void clear() {
    productsInCart.clear();
  }

  public void putProductInCart(Product product, int amount) {
    int numberOfProducts = getCurrentCartCapacity();
    if (numberOfProducts + amount > productLimit) {
      throw new ExceedLimitException();
    }

    Optional<Product> productsAlreadyInCart = productsInCart.keySet().stream().filter(p -> p.getId() == product.getId()).findFirst();
    if (productsAlreadyInCart.isEmpty()) {
      productsInCart.put(product, amount);
    } else {
      productsInCart.put(productsAlreadyInCart.get(), productsInCart.get(productsAlreadyInCart.get()) + amount);
    }
  }

  public Map<Product, Integer> getProductsInCart() {
    return productsInCart;
  }

  private int getCurrentCartCapacity() {
    int numberOfProducts = 0;
    for (Map.Entry<Product, Integer> e : productsInCart.entrySet()) {
      numberOfProducts += e.getValue();
    }

    return numberOfProducts;
  }
}
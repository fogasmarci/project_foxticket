package com.greenfoxacademy.springwebapp.models;

import com.greenfoxacademy.springwebapp.exceptions.cart.ExceedLimitException;
import jakarta.persistence.*;

import java.util.HashMap;
import java.util.Map;

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

    productsInCart.merge(product, amount, Integer::sum);
  }

  public Map<Product, Integer> getProductsInCart() {
    return productsInCart;
  }

  private int getCurrentCartCapacity() {
    return productsInCart.values().stream().mapToInt(Integer::intValue).sum();
  }
}
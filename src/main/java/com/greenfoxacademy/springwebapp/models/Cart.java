package com.greenfoxacademy.springwebapp.models;

import com.greenfoxacademy.springwebapp.exceptions.cart.ExceedLimitException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "cart_product",
      joinColumns = @JoinColumn(name = "cart_id"),
      inverseJoinColumns = @JoinColumn(name = "product_id"))
  private List<Product> products;
  @OneToOne(mappedBy = "cart")
  private User user;
  static final int productLimit = 50;

  public Cart() {
    products = new ArrayList<>();
  }

  public Long getId() {
    return id;
  }

  public List<Product> getProducts() {
    return products;
  }

  public void addProduct(Product product) {
    if (products.size() >= productLimit) {
      throw new ExceedLimitException();
    }
    products.add(product);
    product.addCart(this);
  }

  public void setUser(User user) {
    this.user = user;
  }
}
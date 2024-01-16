package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private int price;
  private int duration;
  private String description;
  @ManyToOne
  private ProductType type;
  @ManyToMany(mappedBy = "products")
  private List<Cart> carts;
  @OneToMany(mappedBy = "product")
  private List<Order> orderList;

  public Product() {
    carts = new ArrayList<>();
  }

  public Product(String name, int price, int duration, String description) {
    this();
    this.name = name;
    this.price = price;
    this.duration = duration;
    this.description = description;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public ProductType getType() {
    return type;
  }

  public void setType(ProductType type) {
    this.type = type;
  }

  public List<Cart> getCarts() {
    return carts;
  }

  public void addCart(Cart cart) {
    if (carts.contains(cart)) {
      cart.addProduct(this);
    }
  }

  public void addOrder(Order order) {
    orderList.add(order);
    order.setProduct(this);
  }
}

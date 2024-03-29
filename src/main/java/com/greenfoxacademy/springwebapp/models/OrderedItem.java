package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "ordered_items")
public class OrderedItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Enumerated(EnumType.STRING)
  private Status status;
  private LocalDateTime expiry;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  private Product product;
  @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  private User user;

  public OrderedItem() {
    status = Status.Not_active;
    expiry = null;
  }

  public Long getId() {
    return id;
  }

  public Status getStatus() {
    return status;
  }

  public LocalDateTime getExpiry() {
    return expiry;
  }

  public Product getProduct() {
    return product;
  }

  public User getUser() {
    return user;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public void setExpiry(LocalDateTime expiry) {
    this.expiry = expiry;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public void setUser(User user) {
    this.user = user;
    user.addOrder(this);
  }

  public void setId(Long id) {
    this.id = id;
  }
}

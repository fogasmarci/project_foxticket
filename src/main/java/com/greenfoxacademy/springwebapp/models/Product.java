package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(unique = true)
  private String name;
  private int price;
  private int duration;
  private String description;
  @ManyToOne
  private ProductType type;

  public Product() {
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

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    Product otherProduct = (Product) obj;
    return Objects.equals(this.name, otherProduct.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}

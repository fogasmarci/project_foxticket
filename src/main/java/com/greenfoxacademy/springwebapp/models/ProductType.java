package com.greenfoxacademy.springwebapp.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product_types")
public class ProductType {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.PERSIST}, mappedBy = "type", fetch = FetchType.LAZY)
  private List<Product> products;

  public ProductType() {
    products = new ArrayList<>();
  }

  public ProductType(String name) {
    this();
    this.name = name;
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

  public List<Product> getProducts() {
    return products;
  }

  public void addProduct(Product product) {
    products.add(product);
    product.setType(this);
  }
}

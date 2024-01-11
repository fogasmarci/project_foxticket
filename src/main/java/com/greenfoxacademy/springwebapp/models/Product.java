package com.greenfoxacademy.springwebapp.models;

import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@NamedNativeQuery(name = "Product.findProductsInUsersCart", query = "SELECT products.id AS product_id, products.name AS name, products.price AS price FROM products JOIN cart_product ON products.id = product_id JOIN carts ON carts.id = cart_id WHERE carts.id = (SELECT carts.id FROM users JOIN carts ON users.cart_id = carts.id WHERE users.id = :userId)", resultSetMapping = "Mapping.CartProductDTO")
@SqlResultSetMapping(name = "Mapping.CartProductDTO",
    classes = @ConstructorResult(targetClass = CartProductDTO.class,
        columns = {@ColumnResult(name = "product_id"),
            @ColumnResult(name = "name"), @ColumnResult(name = "price")}))
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
  @ManyToMany
  private List<Cart> carts;

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
}

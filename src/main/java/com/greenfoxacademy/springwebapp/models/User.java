package com.greenfoxacademy.springwebapp.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  @Column(unique = true)
  private String email;
  @JsonIgnore
  private String password;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> authorities;
  @OneToOne(cascade = CascadeType.ALL)
  private Cart cart;
  @Column(columnDefinition = "boolean default false")
  private boolean isAdmin;
  @Column(columnDefinition = "boolean default false")
  private boolean isVerified;
  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  private List<OrderedItem> orderedItems;

  public User() {
    authorities = new HashSet<>();
    cart = new Cart();
    cart.setUser(this);
    isAdmin = false;
    isVerified = false;
    orderedItems = new ArrayList<>();
  }

  public User(Long userId, String email) {
    this();
    this.id = userId;
    this.email = email;
  }

  public User(String name, String email, String password) {
    this();
    this.name = name;
    this.email = email;
    this.password = password;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Cart getCart() {
    return cart;
  }

  public boolean getIsAdmin() {
    return isAdmin;
  }

  public boolean getIsVerified() {
    return isVerified;
  }

  public void addRole(Role role) {
    authorities.add(role);
  }

  public List<OrderedItem> getOrders() {
    return orderedItems;
  }

  public void addOrder(OrderedItem orderedItem) {
    orderedItems.add(orderedItem);
  }

  public Set<Role> getAuthorities() {
    return authorities;
  }

  public boolean getIsAdminByRoles() {
    return this.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Authorities.ADMIN.toString()));
  }
}

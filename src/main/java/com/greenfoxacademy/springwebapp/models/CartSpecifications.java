package com.greenfoxacademy.springwebapp.models;

import org.springframework.data.jpa.domain.Specification;

public class CartSpecifications {

  public static Specification<Cart> hasUserId(Long userId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
  }
}

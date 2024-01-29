package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.springwebapp.models.OrderedItem;
import com.greenfoxacademy.springwebapp.models.Status;

import java.time.LocalDateTime;

public class OrderedItemDTO {
  private final Long id;
  private final Status status;
  private final LocalDateTime expiry;
  @JsonProperty("product_id")
  private Long productId;

  public OrderedItemDTO(Long id, Status status, LocalDateTime expiry, Long productId) {
    this.id = id;
    this.status = status;
    this.expiry = expiry;
    this.productId = productId;
  }

  public OrderedItemDTO(OrderedItem order) {
    id = order.getId();
    status = order.getStatus();
    expiry = order.getExpiry();
    productId = order.getProduct().getId();
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

  public Long getProductId() {
    return productId;
  }
}

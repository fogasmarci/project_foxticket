package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.springwebapp.models.Status;

import java.time.LocalDateTime;

public class OrderedItemDTO {
  private Long id;
  private Status status;
  private LocalDateTime expiry;
  @JsonProperty("product_id")
  private Long productId;

  public OrderedItemDTO(Long id, Status status, LocalDateTime expiry, Long productId) {
    this.id = id;
    this.status = status;
    this.expiry = expiry;
    this.productId = productId;
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

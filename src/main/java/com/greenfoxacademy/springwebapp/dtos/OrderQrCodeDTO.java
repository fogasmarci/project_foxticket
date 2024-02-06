package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.greenfoxacademy.springwebapp.models.QrCodeDataSerializer;

@JsonSerialize(using = QrCodeDataSerializer.class)
public class OrderQrCodeDTO {

  private NameEmailDTO user;
  private ProductQrCodeDTO product;


  public OrderQrCodeDTO(NameEmailDTO user, ProductQrCodeDTO product) {
    this.user = user;
    this.product = product;
  }

  public NameEmailDTO getUser() {
    return user;
  }

  public ProductQrCodeDTO getProduct() {
    return product;
  }

  public void setUser(NameEmailDTO user) {
    this.user = user;
  }

  public void setProduct(ProductQrCodeDTO product) {
    this.product = product;
  }
}

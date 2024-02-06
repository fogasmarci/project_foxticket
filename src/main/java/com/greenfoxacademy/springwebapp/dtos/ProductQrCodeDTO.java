package com.greenfoxacademy.springwebapp.dtos;

public class ProductQrCodeDTO {
  private String name;
  private String duration;
  private String type;
  private String status;
  private String expirationDate;


  public ProductQrCodeDTO(String name, String duration, String type, String status, String expirationDate) {
    this.name = name;
    this.duration = duration;
    this.type = type;
    this.status = status;
    this.expirationDate = expirationDate;
  }

  public String getName() {
    return name;
  }

  public String getDuration() {
    return duration;
  }

  public String getType() {
    return type;
  }

  public String getStatus() {
    return status;
  }

  public String getExpirationDate() {
    return expirationDate;
  }
}

package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.springwebapp.models.DurationConverter;

import java.time.Duration;

public class ProductWithoutIdDTO {
  private String name;
  private Integer price;
  @JsonIgnore
  private Duration duration;
  @JsonProperty("testing")
  private String durationInString;
  private String description;
  @JsonProperty("item_id")
  private Long typeId;

  public ProductWithoutIdDTO(String name, Integer price, String durationInString, String description, Long typeId) {
    this.name = name;
    this.price = price;
    this.durationInString = durationInString;
    this.description = description;
    this.typeId = typeId;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public Duration getDuration() {
    return duration;
  }

  public String getDescription() {
    return description;
  }

  public Long getTypeId() {
    return typeId;
  }

  public String getDurationInString() {
    return durationInString;
  }

  public void convertDuration() {
    String[] data = durationInString.split(" ");
    switch (data[1]) {
      case DurationConverter.days:
        duration = Duration.ofDays(Integer.parseInt(data[0]));
        break;
      case DurationConverter.hours:
        duration = Duration.ofHours(Integer.parseInt(data[0]));
        break;
      case DurationConverter.minutes:
        duration = Duration.ofMinutes(Integer.parseInt(data[0]));
        break;
      default:
        duration = Duration.ZERO;
        break;
    }
  }
}
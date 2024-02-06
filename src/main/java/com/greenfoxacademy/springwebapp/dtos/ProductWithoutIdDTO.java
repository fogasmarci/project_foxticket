package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.greenfoxacademy.springwebapp.models.DurationConverter;

import java.time.Duration;

public record ProductWithoutIdDTO(String name, Integer price, String durationInString, String description,
                                  @JsonProperty("item_id")
                                  Long typeId) {

  public Duration duration() {
    return DurationConverter.convertDateToDuration(durationInString);
  }
}
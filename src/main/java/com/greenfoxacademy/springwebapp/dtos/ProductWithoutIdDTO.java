package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ProductWithoutIdDTO(String name, Integer price, String durationInString, String description,
                                  @JsonProperty("item_id")
                                  Long typeId) {
}
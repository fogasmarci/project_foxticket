package com.greenfoxacademy.springwebapp.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Duration;

public record ProductWithoutIdDTO(String name, Integer price, Duration duration, String description,
                                  @JsonProperty("item_id") Long typeId) {}
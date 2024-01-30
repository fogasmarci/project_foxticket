package com.greenfoxacademy.springwebapp.dtos;

import java.time.Duration;

public record ProductDTO(Long id, String name, int price, Duration duration, String description, String type) {
}
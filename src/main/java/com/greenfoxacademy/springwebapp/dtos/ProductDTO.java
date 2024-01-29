package com.greenfoxacademy.springwebapp.dtos;

public record ProductDTO(Long id, String name, int price, int duration, String description, String type) {
}
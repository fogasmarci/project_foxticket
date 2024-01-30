package com.greenfoxacademy.springwebapp.dtos;

import java.util.List;

public record OrderListDTO(List<OrderedItemDTO> orders) {
}

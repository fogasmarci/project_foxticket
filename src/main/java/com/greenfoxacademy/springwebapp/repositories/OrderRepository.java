package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<OrderedItem, Long> {
}

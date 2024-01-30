package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderedItem, Long> {
  Optional<OrderedItem> findById(Long id);
}

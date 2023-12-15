package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Override
  List<Product> findAll();
}

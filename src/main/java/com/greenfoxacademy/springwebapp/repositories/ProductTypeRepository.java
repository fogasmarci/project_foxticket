package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.models.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}

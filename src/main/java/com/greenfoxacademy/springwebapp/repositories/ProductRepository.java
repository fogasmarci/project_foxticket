package com.greenfoxacademy.springwebapp.repositories;

import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Override
  List<Product> findAll();

  Optional<Product> findById(Long productId);

  @Query(nativeQuery = true)
  List<CartProductDTO> findProductsInUsersCart(Long userId);
}

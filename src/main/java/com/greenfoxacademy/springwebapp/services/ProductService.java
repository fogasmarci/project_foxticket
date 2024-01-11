package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.CartProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.models.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

  ProductListDTO listProductDetails();

  Optional<Product> findProductById(Long productId);

  List<CartProductDTO> findProductsInUsersCart(Long userId);
}

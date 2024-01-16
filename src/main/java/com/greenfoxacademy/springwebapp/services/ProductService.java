package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;

import java.util.Optional;

public interface ProductService {

  Product findProductByName(String name);

  ProductType findProductTypeById(Long id);

  ProductListDTO listProductDetails();

  ProductDTO createProductDTO(Product product);

  ProductDTO createProduct(ProductWithoutIdDTO productWithoutIdDTO);

  Optional<Product> findProductById(Long productId);

  ProductDTO editProduct(ProductWithoutIdDTO productWithoutIdDTO, Long productId);

  MessageDTO deleteProduct(Long productId);
}

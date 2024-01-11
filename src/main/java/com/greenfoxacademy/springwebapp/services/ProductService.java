package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTOWithoutID;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;

import java.util.Optional;

public interface ProductService {

  Product findProductByName(String name);

  ProductType findProductTypeById(Long id);

  ProductListDTO listProductDetails();

  ProductDTO createProductDTO(Product product);

  Product createProduct(ProductDTOWithoutID productDTOWithoutID);

  Optional<Product> findProductById(Long productId);
}

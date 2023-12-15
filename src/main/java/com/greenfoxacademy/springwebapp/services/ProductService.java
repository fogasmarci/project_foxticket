package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTOWithoutID;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.models.Product;

public interface ProductService {

  ProductListDTO listProductDetails();

  ProductDTO createProductDTO(Product product);

  Product createProduct(ProductDTOWithoutID productDTOWithoutID);
}
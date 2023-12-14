package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
  private ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @RequestMapping(path = "/api/products", method = RequestMethod.GET)
  public ResponseEntity<?> getProductDetails() {
      return ResponseEntity.status(200).body(productService.listProductDetails());
  }
}

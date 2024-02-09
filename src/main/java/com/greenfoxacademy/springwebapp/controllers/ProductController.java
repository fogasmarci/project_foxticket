package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
import com.greenfoxacademy.springwebapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
  private final ProductService productService;

  @Autowired
  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @GetMapping("/api/products")
  public ResponseEntity<ProductListDTO> getProductDetails() {
    return ResponseEntity.status(200).body(productService.listProductDetails());
  }

  @PostMapping("/api/products")
  public ResponseEntity<ProductDTO> addNewProduct(@RequestBody ProductWithoutIdDTO productWithoutIdDTO) {
    return ResponseEntity.status(200).body(productService.createProduct(productWithoutIdDTO));
  }

  @PatchMapping(path = "/api/products/{productId}")
  public ResponseEntity<ProductDTO> editProduct(@PathVariable Long productId, @RequestBody ProductWithoutIdDTO productWithoutIdDTO) {
    return ResponseEntity.status(200).body(productService.editProduct(productWithoutIdDTO, productId));
  }

  @DeleteMapping("/api/products/{productId}")
  public ResponseEntity<MessageDTO> deleteProduct(@PathVariable Long productId) {
    return ResponseEntity.status(200).body(productService.deleteProduct(productId));
  }
}

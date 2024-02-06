package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.durationconverter.DurationIsMalformedException;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.ProductTypeException;
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
  public ResponseEntity<?> getProductDetails() {
    return ResponseEntity.status(200).body(productService.listProductDetails());
  }

  @PostMapping("/api/products")
  public ResponseEntity<?> addNewProduct(@RequestBody ProductWithoutIdDTO productWithoutIdDTO) {
    try {
      return ResponseEntity.status(200).body(productService.createProduct(productWithoutIdDTO));
    } catch (FieldsException | ProductException | ProductTypeException | DurationIsMalformedException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @PatchMapping(path = "/api/products/{productId}")
  public ResponseEntity<?> editProduct(@PathVariable Long productId, @RequestBody ProductWithoutIdDTO productWithoutIdDTO) {
    try {
      return ResponseEntity.status(200).body(productService.editProduct(productWithoutIdDTO, productId));
    } catch (FieldsException | ProductException | ProductTypeException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @DeleteMapping("/api/products/{productId}")
  public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
    try {
      return ResponseEntity.status(200).body(productService.deleteProduct(productId));
    } catch (ProductIdInvalidException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

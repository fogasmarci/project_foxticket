package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeResponseDTO;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductTypeRestController {
  private final ProductTypeService productTypeService;

  @Autowired
  public ProductTypeRestController(ProductTypeService productTypeService) {
    this.productTypeService = productTypeService;
  }

  @PostMapping("/api/product-types")
  public ResponseEntity<ProductTypeResponseDTO> addProductType(@RequestBody NameDTO nameDTO) {
    return ResponseEntity.status(200).body(productTypeService.addProductType(nameDTO));
  }
}

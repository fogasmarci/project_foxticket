package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.ProductTypeNameAlreadyExist;
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

  @PostMapping(path = "/api/product-types")
  public ResponseEntity<?> addProductType(@RequestBody NameDTO nameDTO) {
    try {
      return ResponseEntity.status(200).body(productTypeService.addProductType(nameDTO));
    } catch (FieldsException | ProductTypeNameAlreadyExist e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

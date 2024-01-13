package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTOWithoutID;
import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.ProductTypeException;
import com.greenfoxacademy.springwebapp.services.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

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

  @RequestMapping(path = "/api/products", method = RequestMethod.POST)
  public ResponseEntity<?> addNewProduct(@RequestBody ProductDTOWithoutID productDTOWithoutID) {
    try {
      return ResponseEntity.status(200).body(productService.createProduct(productDTOWithoutID));
    } catch (MissingFieldsException | ProductException | ProductTypeException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @RequestMapping(path = "/api/products/{productId}", method = RequestMethod.PATCH)
  public ResponseEntity<?> editProduct(@PathVariable Long productId, @RequestBody ProductDTOWithoutID productDTOWithoutID) {
    try {
      return ResponseEntity.status(200).body(productService.editProduct(productDTOWithoutID, productId));
    } catch (MissingFieldsException | ProductException | ProductTypeException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

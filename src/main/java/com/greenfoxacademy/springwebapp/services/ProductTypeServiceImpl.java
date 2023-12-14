package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
  private ProductTypeRepository productTypeRepository;

  @Autowired
  public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
    this.productTypeRepository = productTypeRepository;
  }

  @Override
  public String getHelloWorld() {
    return null;
  }
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.ProductTypeNameAlreadyExist;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {

  private final ProductTypeRepository productTypeRepository;

  @Autowired
  public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
    this.productTypeRepository = productTypeRepository;
  }

  @Override
  public ProductType addProductType(NameDTO nameDTO) {
    if (nameDTO.getName() == null) {
      throw new FieldsException("Name is required");
    }
    ProductType existingProductType = productTypeRepository.findByName(nameDTO.getName()).orElse(null);
    if (existingProductType != null) {
      throw new ProductTypeNameAlreadyExist();
    }
    return productTypeRepository.save(new ProductType(nameDTO.getName()));
  }
}

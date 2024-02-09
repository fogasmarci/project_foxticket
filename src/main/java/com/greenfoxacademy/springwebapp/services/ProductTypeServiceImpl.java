package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.taken.ProductTypeNameAlreadyExist;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductTypeServiceImpl implements ProductTypeService {
  private final ProductTypeRepository productTypeRepository;

  @Autowired
  public ProductTypeServiceImpl(ProductTypeRepository productTypeRepository) {
    this.productTypeRepository = productTypeRepository;
  }

  @Override
  public ProductTypeResponseDTO addProductType(NameDTO nameDTO) {
    if (nameDTO.getName().isBlank()) {
      throw new FieldsException("Name is required");
    }

    boolean isProductTypePresent = productTypeRepository.existsByName(nameDTO.getName());
    if (isProductTypePresent) {
      throw new ProductTypeNameAlreadyExist();
    }

    ProductType newProductType = new ProductType(nameDTO.getName());
    productTypeRepository.save(newProductType);
    return new ProductTypeResponseDTO(newProductType.getId(), newProductType.getName());
  }

  @Override
  public Optional<ProductType> findProductTypeById(Long id) {
    return productTypeRepository.findById(id);
  }
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeResponseDTO;
import com.greenfoxacademy.springwebapp.models.ProductType;

public interface ProductTypeService {
  ProductTypeResponseDTO addProductType(NameDTO nameDTO);
}

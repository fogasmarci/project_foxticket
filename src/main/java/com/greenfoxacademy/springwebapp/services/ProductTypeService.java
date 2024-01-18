package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductTypeResponseDTO;

public interface ProductTypeService {
  ProductTypeResponseDTO addProductType(NameDTO nameDTO);
}

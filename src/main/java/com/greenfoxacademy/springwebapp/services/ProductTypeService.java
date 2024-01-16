package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.NameDTO;
import com.greenfoxacademy.springwebapp.models.ProductType;

public interface ProductTypeService {
  ProductType addProductType(NameDTO nameDTO);
}

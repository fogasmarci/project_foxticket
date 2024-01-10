package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTOWithoutID;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductNameAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.InvalidProductTypeException;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
  private ProductRepository productRepository;
  private ProductTypeRepository productTypeRepository;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository, ProductTypeRepository productTypeRepository) {
    this.productRepository = productRepository;
    this.productTypeRepository = productTypeRepository;
  }

  public Product findProductByName(String name) {
    return productRepository.findByName(name).orElse(null);
  }

  public ProductType findProductTypeById(Long id) {
    return productTypeRepository.findById(id).orElse(null);
  }

  @Override
  public ProductListDTO listProductDetails() {
    List<Product> productsList = new ArrayList<>(productRepository.findAll());
    List<ProductDTO> productsDTOList = new ArrayList<>();

    for (Product product : productsList) {
      productsDTOList.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(),
          product.getDescription(), product.getType().getName()));
    }

    ProductListDTO productListDTO = new ProductListDTO(productsDTOList);

    return productListDTO;
  }

  public ProductDTO createProductDTO(Product product) {
    return new ProductDTO(product.getId(), product.getName(), product.getPrice(),
        product.getDuration(), product.getDescription(), product.getType().getName());
  }

  public Product createProduct(ProductDTOWithoutID productDTOWithoutID) {
    if (productDTOWithoutID.getName().isEmpty()) {
      throw new MissingFieldsException("Name is missing");
    }
    if (productDTOWithoutID.getDescription().isEmpty()) {
      throw new MissingFieldsException("Description is missing");
    }
    if (!(productDTOWithoutID.getPrice() > 0)) {
      throw new MissingFieldsException("Price is missing");
    }
    if (!(productDTOWithoutID.getDuration() > 0)) {
      throw new MissingFieldsException("Duration is missing");
    }
    if (!(productDTOWithoutID.getTypeId() > 0)) {
      throw new MissingFieldsException("Type ID is missing");
    }

    if (!(findProductByName(productDTOWithoutID.getName()) == null)) {
      throw new ProductNameAlreadyTakenException();
    }

    if (findProductTypeById(productDTOWithoutID.getTypeId()) == null) {
      throw new InvalidProductTypeException();
    }

    Product productToSave = new Product(productDTOWithoutID.getName(), productDTOWithoutID.getPrice(), productDTOWithoutID.getDuration(), productDTOWithoutID.getDescription());
    ProductType productType = findProductTypeById(productDTOWithoutID.getTypeId());
    productToSave.setType(productType);
    productRepository.save(productToSave);

    return productToSave;
  }
}
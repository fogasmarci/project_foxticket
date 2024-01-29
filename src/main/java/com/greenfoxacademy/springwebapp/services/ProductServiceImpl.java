package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductNameAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.producttype.InvalidProductTypeException;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
  private final ProductRepository productRepository;
  private final ProductTypeService productTypeService;

  @Autowired
  public ProductServiceImpl(ProductRepository productRepository, ProductTypeService productTypeService) {
    this.productRepository = productRepository;
    this.productTypeService = productTypeService;
  }

  @Override
  public Optional<Product> findProductById(Long productId) {
    return productRepository.findById(productId);
  }

  @Override
  public ProductListDTO listProductDetails() {
    List<Product> productsList = new ArrayList<>(productRepository.findAll());
    List<ProductDTO> productsDTOList = new ArrayList<>();

    for (Product product : productsList) {
      productsDTOList.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(),
          product.getDescription(), product.getType().getName()));
    }

    return new ProductListDTO(productsDTOList);
  }

  @Override
  public ProductDTO createProduct(ProductWithoutIdDTO productWithoutIdDTO) {
    productWithoutIdDTO.convertDuration();
    validateProductDTO(productWithoutIdDTO);

    ProductType productType = productTypeService.findProductTypeById(productWithoutIdDTO.getTypeId()).orElseThrow(InvalidProductTypeException::new);
    if (productRepository.existsByName(productWithoutIdDTO.getName())) {
      throw new ProductNameAlreadyTakenException();
    }

    Product productToSave = new Product(productWithoutIdDTO.getName(),
        productWithoutIdDTO.getPrice(), productWithoutIdDTO.getDuration(), productWithoutIdDTO.getDescription());
    productToSave.setType(productType);
    productRepository.save(productToSave);

    return createProductDTO(productToSave);
  }

  @Override
  public ProductDTO createProductDTO(Product product) {
    return new ProductDTO(product.getId(), product.getName(), product.getPrice(),
        product.getDuration(), product.getDescription(), product.getType().getName());
  }

  @Override
  public ProductDTO editProduct(ProductWithoutIdDTO productWithoutIdDTO, Long productId) {
    productWithoutIdDTO.convertDuration();
    validateProductDTO(productWithoutIdDTO);
    Product productToEdit = findProductById(productId).orElseThrow(ProductIdInvalidException::new);

    ProductType productType = productTypeService.findProductTypeById(productWithoutIdDTO.getTypeId()).orElseThrow(InvalidProductTypeException::new);
    if (productRepository.existsByName(productWithoutIdDTO.getName()) && !productToEdit.getName().equals(productWithoutIdDTO.getName())) {
      throw new ProductNameAlreadyTakenException();
    }

    productToEdit.setName(productWithoutIdDTO.getName());
    productToEdit.setPrice(productWithoutIdDTO.getPrice());
    productToEdit.setDuration(productWithoutIdDTO.getDuration());
    productToEdit.setDescription(productWithoutIdDTO.getDescription());
    productToEdit.setType(productType);

    productRepository.save(productToEdit);

    return createProductDTO(productToEdit);
  }

  @Override
  public MessageDTO deleteProduct(Long productId) {
    Product productToDelete = findProductById(productId)
        .orElseThrow(ProductIdInvalidException::new);

    productRepository.delete(productToDelete);
    String okMessage = String.format("Product %s is deleted.", productToDelete.getName());

    return new MessageDTO(okMessage);
  }

  private void validateProductDTO(ProductWithoutIdDTO productWithoutIdDTO) {
    if (productWithoutIdDTO.getName().isEmpty()) {
      throw new FieldsException("Name is missing");
    }
    if (productWithoutIdDTO.getDescription().isEmpty()) {
      throw new FieldsException("Description is missing");
    }
    if (productWithoutIdDTO.getPrice() == null) {
      throw new FieldsException("Price is missing");
    }
    if (productWithoutIdDTO.getDurationInString() == null) {
      throw new FieldsException("Duration is missing");
    }
    if (productWithoutIdDTO.getTypeId() == null) {
      throw new FieldsException("Type ID is missing");
    }
  }
}

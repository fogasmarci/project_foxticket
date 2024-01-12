package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTOWithoutID;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductNameAlreadyTakenException;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ActiveProfiles("test")
public class ProductServiceTest {
  private ProductRepository productRepository;
  private ProductTypeRepository productTypeRepository;
  private ProductServiceImpl productService;

  @BeforeEach
  public void ProductServiceTest() {
    productRepository = Mockito.mock(ProductRepository.class);
    productTypeRepository = Mockito.mock(ProductTypeRepository.class);
    productService = new ProductServiceImpl(productRepository, productTypeRepository);
  }

  @Test
  void listProductDetails_ReturnsProductListDtoWithAllProducts() {
    ProductType productType1 = new ProductType("jegy");
    ProductType productType2 = new ProductType("bérlet");
    Product product1 = new Product("teszt jegy 1", 480, 90, "teszt1");
    Product product2 = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    Product product3 = new Product("teszt bérlet 2", 9500, 9000, "teszt3");
    productType1.addProduct(product1);
    productType2.addProduct(product2);
    productType2.addProduct(product3);
    List<Product> products = List.of(product1, product2, product3);

    Mockito.when(productRepository.findAll()).thenReturn(products);

    List<ProductDTO> productsDTO = new ArrayList<>();
    for (Product product : products) {
      productsDTO.add(new ProductDTO(product.getId(), product.getName(), product.getPrice(), product.getDuration(),
          product.getDescription(), product.getType().getName()));
    }

    ProductListDTO productListDTO = new ProductListDTO(productsDTO);
    assertThat(productService.listProductDetails()).usingRecursiveComparison().isEqualTo(productListDTO);
  }

  @Test
  void createProduct_ProductIsSuccessfullySaved() {
    ProductDTOWithoutID productDTOWithoutID = new ProductDTOWithoutID("new product", 480, 90, "teszt1", 2L);
    Product product = new Product(productDTOWithoutID.getName(),
        productDTOWithoutID.getPrice(), productDTOWithoutID.getDuration(), productDTOWithoutID.getDescription());
    ProductType berlet = new ProductType("bérlet");
    product.setType(berlet);
    
    Mockito.when(productRepository.findByName(productDTOWithoutID.getName())).thenReturn(Optional.empty());
    Mockito.when(productTypeRepository.findById(productDTOWithoutID.getTypeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

    ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(),
        product.getDuration(), product.getDescription(), product.getType().getName());
    assertThat(productService.createProduct(productDTOWithoutID)).usingRecursiveComparison().isEqualTo(productDTO);

    verify(productRepository, times(1)).findByName(productDTOWithoutID.getName());
    verify(productRepository, times(1)).save(Mockito.any(Product.class));
  }

  @Test
  void createProduct_WithEmptyNameField_ThrowsCorrectException() {
    ProductDTOWithoutID productDTOWithoutID =
        new ProductDTOWithoutID("", 480, 90, "teszt1", 2L);

    Throwable exception = assertThrows(MissingFieldsException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Name is missing", exception.getMessage());
  }

  @Test
  void createProduct_WithExistingProductName_ThrowsCorrectException() {
    ProductDTOWithoutID productDTOWithoutID =
        new ProductDTOWithoutID("new product", 480, 90, "teszt1", 2L);
    Product product = new Product(productDTOWithoutID.getName(),
        productDTOWithoutID.getPrice(), productDTOWithoutID.getDuration(), productDTOWithoutID.getDescription());
    ProductType berlet = new ProductType("bérlet");
    product.setType(berlet);

    Mockito.when(productRepository.findByName(productDTOWithoutID.getName())).thenReturn(Optional.of(product));

    Throwable exception =
        assertThrows(ProductNameAlreadyTakenException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Product name already exists.", exception.getMessage());
  }
}
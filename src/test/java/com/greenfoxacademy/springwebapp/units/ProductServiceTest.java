package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.repositories.ProductTypeRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
public class ProductServiceTest {
  private ProductRepository productRepository;
  private ProductTypeRepository productTypeRepository;
  private ProductServiceImpl productService;

  public ProductServiceTest() {
    productRepository = Mockito.mock(ProductRepository.class);

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
}
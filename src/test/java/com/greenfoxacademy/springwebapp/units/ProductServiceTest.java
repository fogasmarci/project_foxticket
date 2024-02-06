package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
import com.greenfoxacademy.springwebapp.exceptions.durationconverter.DurationIsMalformedException;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductIdInvalidException;
import com.greenfoxacademy.springwebapp.exceptions.product.ProductNameAlreadyTakenException;
import com.greenfoxacademy.springwebapp.models.Product;
import com.greenfoxacademy.springwebapp.models.ProductType;
import com.greenfoxacademy.springwebapp.repositories.ProductRepository;
import com.greenfoxacademy.springwebapp.services.ProductServiceImpl;
import com.greenfoxacademy.springwebapp.services.ProductTypeService;
import com.greenfoxacademy.springwebapp.services.ProductTypeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
public class ProductServiceTest {
  ProductRepository productRepository;
  ProductTypeService productTypeService;
  ProductServiceImpl productService;

  @BeforeEach
  public void productServiceTests() {
    productRepository = Mockito.mock(ProductRepository.class);
    productTypeService = Mockito.mock(ProductTypeServiceImpl.class);
    productService = new ProductServiceImpl(productRepository, productTypeService);
  }

  @Test
  void listProductDetails_ReturnsProductListDtoWithAllProducts() {
    ProductType productType1 = new ProductType("jegy");
    ProductType productType2 = new ProductType("bérlet");
    Product product1 = new Product("teszt jegy 1", 480, Duration.ofHours(1), "teszt1");
    Product product2 = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
    Product product3 = new Product("teszt bérlet 2", 9500, Duration.ofDays(30), "teszt3");
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
  void createProduct_DateIsNotFormattedCorrectly_ThrowsCorrectException() {
    ProductWithoutIdDTO productDTOWithoutID =
        new ProductWithoutIdDTO("jegy", 480, "60 years", "teszt1", 2L);

    ProductType berlet = new ProductType("bérlet");
    Mockito.when(productTypeService.findProductTypeById(productDTOWithoutID.typeId())).thenReturn(Optional.of(berlet));

    Throwable exception = assertThrows(DurationIsMalformedException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Duration is not valid.", exception.getMessage());
  }

  @Test
  void createProduct_ProductIsSuccessfullySaved() {
    ProductWithoutIdDTO productWithoutIdDTO = new ProductWithoutIdDTO("new product", 480, "60 minutes", "teszt1", 2L);
    Product product = mapDTOToProduct(productWithoutIdDTO);
    ProductType berlet = new ProductType("bérlet");
    product.setType(berlet);

    Mockito.when(productRepository.findByName(productWithoutIdDTO.name())).thenReturn(Optional.empty());
    Mockito.when(productTypeService.findProductTypeById(productWithoutIdDTO.typeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

    ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(),
        product.getDuration(), product.getDescription(), product.getType().getName());
    assertThat(productService.createProduct(productWithoutIdDTO)).usingRecursiveComparison().isEqualTo(productDTO);

    verify(productRepository, times(1)).existsByName(productWithoutIdDTO.name());
    verify(productRepository, times(1)).save(Mockito.any(Product.class));
  }

  @Test
  void createProduct_WithEmptyNameField_ThrowsCorrectException() {
    ProductWithoutIdDTO productDTOWithoutID =
        new ProductWithoutIdDTO("", 480, "60 minutes", "teszt1", 2L);

    Throwable exception = assertThrows(FieldsException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Name is missing", exception.getMessage());
  }

  @Test
  void createProduct_WithExistingProductName_ThrowsCorrectException() {
    ProductWithoutIdDTO productDTOWithoutID =
        new ProductWithoutIdDTO("new product", 480, "60 minutes", "teszt1", 2L);
    Product product = mapDTOToProduct(productDTOWithoutID);
    ProductType berlet = new ProductType("bérlet");
    product.setType(berlet);

    Mockito.when(productRepository.existsByName(productDTOWithoutID.name())).thenReturn(true);
    Mockito.when(productTypeService.findProductTypeById(productDTOWithoutID.typeId())).thenReturn(Optional.of(berlet));

    Throwable exception =
        assertThrows(ProductNameAlreadyTakenException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Product name already exists.", exception.getMessage());
  }

  @Test
  void deleteProduct_WithExistingProductId_ProductIsDeleted() {
    Long productId = 1L;
    Product productToDelete = new Product("vonaljegy", 480, Duration.ofHours(1), "teszt1");
    ProductType ticket = new ProductType("jegy");
    productToDelete.setType(ticket);

    Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(productToDelete));
    Mockito.doNothing().when(productRepository).delete(productToDelete);

    MessageDTO result = productService.deleteProduct(productId);

    assertEquals("Product vonaljegy is deleted.", result.message());
  }

  @Test
  void deleteProduct_WithInvalidProductId_ThrowsCorrectException() {
    Long productId = 11L;

    Throwable exception =
        assertThrows(ProductIdInvalidException.class, () -> productService.deleteProduct(productId));
    assertEquals("Product doesn't exist.", exception.getMessage());

    verify(productRepository, never()).delete(any());
  }

  @Test
  void editProduct_ProductNameIsNotChanged_ProductIsSuccessfullyEdited() {
    Long productToEditId = 2L;
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("teszt bérlet 1", 480, "30 days", "teszt1", 2L);

    Product productToEdit = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
    ProductType berlet = new ProductType("bérlet");
    productToEdit.setType(berlet);

    Mockito.when(productRepository.findById(productToEditId)).thenReturn(Optional.of(productToEdit));
    Mockito.when(productTypeService.findProductTypeById(newProductDetails.typeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.existsByName(newProductDetails.name())).thenReturn(true);
    Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productToEdit);

    productToEdit.setName(newProductDetails.name());
    productToEdit.setPrice(newProductDetails.price());
    productToEdit.setDuration(newProductDetails.duration());
    productToEdit.setDescription(newProductDetails.description());
    productToEdit.setType(berlet);

    ProductDTO productDTO = new ProductDTO(productToEdit.getId(), productToEdit.getName(), productToEdit.getPrice(),
        productToEdit.getDuration(), productToEdit.getDescription(), productToEdit.getType().getName());
    assertThat(productService.editProduct(newProductDetails, productToEditId)).usingRecursiveComparison().isEqualTo(productDTO);

    verify(productRepository, times(1)).existsByName(newProductDetails.name());
    verify(productRepository, times(1)).save(Mockito.any(Product.class));
  }

  @Test
  void editProduct_ExistingProductNameIsGiven_ThrowsCorrectException() {
    Long productToEditId = 2L;
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("teszt jegy 1", 480, "60 minutes", "teszt1", 2L);

    Product productToEdit = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
    ProductType berlet = new ProductType("bérlet");
    productToEdit.setType(berlet);

    Mockito.when(productRepository.findById(productToEditId)).thenReturn(Optional.of(productToEdit));
    Mockito.when(productTypeService.findProductTypeById(newProductDetails.typeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.existsByName(newProductDetails.name())).thenReturn(true);

    Throwable exception =
        assertThrows(ProductNameAlreadyTakenException.class, () -> productService.editProduct(newProductDetails, productToEditId));
    assertEquals("Product name already exists.", exception.getMessage());
  }

  @Test
  void editProduct_ProductNameIsMissing_ThrowsCorrectException() {
    Long productToEditId = 2L;
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("", 480, "60 minutes", "teszt1", 2L);

    Product productToEdit = new Product("teszt bérlet 1", 4000, Duration.ofDays(30), "teszt2");
    ProductType berlet = new ProductType("bérlet");
    productToEdit.setType(berlet);

    Throwable exception =
        assertThrows(FieldsException.class, () -> productService.editProduct(newProductDetails, productToEditId));
    assertEquals("Name is missing", exception.getMessage());
  }

  private Product mapDTOToProduct(ProductWithoutIdDTO productDTOWithoutID) {
    return new Product(productDTOWithoutID.name(),
        productDTOWithoutID.price(), productDTOWithoutID.duration(), productDTOWithoutID.description());
  }
}

package com.greenfoxacademy.springwebapp.units;

import com.greenfoxacademy.springwebapp.dtos.ProductDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductListDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
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
  public void productServiceTests() {
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
    ProductWithoutIdDTO productWithoutIdDTO = new ProductWithoutIdDTO("new product", 480, 90, "teszt1", 2L);
    Product product = mapDTOToProduct(productWithoutIdDTO);
    ProductType berlet = new ProductType("bérlet");
    product.setType(berlet);

    Mockito.when(productRepository.findByName(productWithoutIdDTO.getName())).thenReturn(Optional.empty());
    Mockito.when(productTypeRepository.findById(productWithoutIdDTO.getTypeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(product);

    ProductDTO productDTO = new ProductDTO(product.getId(), product.getName(), product.getPrice(),
        product.getDuration(), product.getDescription(), product.getType().getName());
    assertThat(productService.createProduct(productWithoutIdDTO)).usingRecursiveComparison().isEqualTo(productDTO);

    verify(productRepository, times(1)).findByName(productWithoutIdDTO.getName());
    verify(productRepository, times(1)).save(Mockito.any(Product.class));
  }

  @Test
  void createProduct_WithEmptyNameField_ThrowsCorrectException() {
    ProductWithoutIdDTO productDTOWithoutID =
        new ProductWithoutIdDTO("", 480, 90, "teszt1", 2L);

    Throwable exception = assertThrows(MissingFieldsException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Name is missing", exception.getMessage());
  }

  @Test
  void createProduct_WithExistingProductName_ThrowsCorrectException() {
    ProductWithoutIdDTO productDTOWithoutID =
        new ProductWithoutIdDTO("new product", 480, 90, "teszt1", 2L);
    Product product = mapDTOToProduct(productDTOWithoutID);
    ProductType berlet = new ProductType("bérlet");
    product.setType(berlet);

    Mockito.when(productRepository.findByName(productDTOWithoutID.getName())).thenReturn(Optional.of(product));
    Mockito.when(productTypeRepository.findById(productDTOWithoutID.getTypeId())).thenReturn(Optional.of(berlet));

    Throwable exception =
        assertThrows(ProductNameAlreadyTakenException.class, () -> productService.createProduct(productDTOWithoutID));
    assertEquals("Product name already exists.", exception.getMessage());
  }

  @Test
  void editProduct_ProductNameIsNotChanged_ProductIsSuccessfullyEdited() {
    Long productToEditId = 2L;
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("teszt bérlet 1", 480, 90, "teszt1", 2L);

    Product productToEdit = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductType berlet = new ProductType("bérlet");
    productToEdit.setType(berlet);

    Mockito.when(productRepository.findById(productToEditId)).thenReturn(Optional.of(productToEdit));
    Mockito.when(productTypeRepository.findById(newProductDetails.getTypeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.findByName(newProductDetails.getName())).thenReturn(Optional.of(productToEdit));
    Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(productToEdit);

    productToEdit.setName(newProductDetails.getName());
    productToEdit.setPrice(newProductDetails.getPrice());
    productToEdit.setDuration(newProductDetails.getDuration());
    productToEdit.setDescription(newProductDetails.getDescription());
    productToEdit.setType(berlet);

    ProductDTO productDTO = new ProductDTO(productToEdit.getId(), productToEdit.getName(), productToEdit.getPrice(),
        productToEdit.getDuration(), productToEdit.getDescription(), productToEdit.getType().getName());
    assertThat(productService.editProduct(newProductDetails, productToEditId)).usingRecursiveComparison().isEqualTo(productDTO);

    verify(productRepository, times(1)).findByName(newProductDetails.getName());
    verify(productRepository, times(1)).save(Mockito.any(Product.class));
  }

  @Test
  void editProduct_ExistingProductNameIsGiven_ThrowsCorrectException() {
    Long productToEditId = 2L;
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("teszt jegy 1", 480, 90, "teszt1", 2L);

    Product productToEdit = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductType berlet = new ProductType("bérlet");
    productToEdit.setType(berlet);

    Mockito.when(productRepository.findById(productToEditId)).thenReturn(Optional.of(productToEdit));
    Mockito.when(productTypeRepository.findById(newProductDetails.getTypeId())).thenReturn(Optional.of(berlet));
    Mockito.when(productRepository.findByName(newProductDetails.getName())).thenReturn(Optional.of(productToEdit));

    Throwable exception =
        assertThrows(ProductNameAlreadyTakenException.class, () -> productService.editProduct(newProductDetails, productToEditId));
    assertEquals("Product name already exists.", exception.getMessage());
  }

  @Test
  void editProduct_ProductNameIsMissing_ThrowsCorrectException() {
    Long productToEditId = 2L;
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("", 480, 90, "teszt1", 2L);

    Product productToEdit = new Product("teszt bérlet 1", 4000, 9000, "teszt2");
    ProductType berlet = new ProductType("bérlet");
    productToEdit.setType(berlet);

    Throwable exception =
        assertThrows(MissingFieldsException.class, () -> productService.editProduct(newProductDetails, productToEditId));
    assertEquals("Name is missing", exception.getMessage());
  }

  private Product mapDTOToProduct(ProductWithoutIdDTO productDTOWithoutID) {
    return new Product(productDTOWithoutID.getName(),
        productDTOWithoutID.getPrice(), productDTOWithoutID.getDuration(), productDTOWithoutID.getDescription());
  }
}

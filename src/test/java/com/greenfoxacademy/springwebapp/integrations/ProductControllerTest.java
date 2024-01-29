package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductWithoutIdDTO;
import com.greenfoxacademy.springwebapp.services.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.isA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  ProductService productService;
  @Autowired
  ObjectMapper objectMapper;

  @Test
  void getProductDetails_ListsAllProducts() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    mvc.perform(get("/api/products").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.products", isA(ArrayList.class)))
        .andExpect(jsonPath("$.products").value(hasSize(3)))
        .andExpect(jsonPath("$.products[0].name").value("teszt jegy 1"))
        .andExpect(jsonPath("$.products[1].description").value("teszt2"));
  }

  @Test
  void addNewProduct_WithValidRequest_ReturnsCorrectJson() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO productWithoutIdDTO = new ProductWithoutIdDTO("1 week pass", 12000,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(post("/api/products")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productWithoutIdDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.name").value("1 week pass"))
        .andExpect(jsonPath("$.price").value(12000))
        .andExpect(jsonPath("$.type").value("jegy"));
  }

  @Test
  void addNewProduct_WithMissingPriceField_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO productWithoutIdDTO = new ProductWithoutIdDTO("1 week pass", null,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(post("/api/products")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productWithoutIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Price is missing"));
  }

  @Test
  void addNewProduct_WithLoggedUser_ReturnsForbidden() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO productWithoutIdDTO = new ProductWithoutIdDTO("1 week pass", 12000,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(post("/api/products").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productWithoutIdDTO)))
        .andExpect(status().is(403));
  }

  @Test
  void deleteProduct_WithValidProductId_DeleteIsSuccessful() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);

    mvc.perform(delete("/api/products/1").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.message").value("Product teszt jegy 1 is deleted."));
  }

  @Test
  void deleteProduct_WithInvalidProductId_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);

    mvc.perform(delete("/api/products/111").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product doesn't exist."));
  }

  @Test
  void deleteProduct_WithLoggedUser_ReturnsForbidden() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);

    mvc.perform(delete("/api/products/1").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(403));
  }

  @Test
  void editProduct_WithValidRequest_ReturnsCorrectJson() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("1 week pass", 12000,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/1")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProductDetails)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.name").value("1 week pass"))
        .andExpect(jsonPath("$.price").value(12000))
        .andExpect(jsonPath("$.type").value("jegy"));
  }

  @Test
  void editProduct_WithInvalidProductId_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("1 week pass", 12000,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/11")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProductDetails)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product doesn't exist."));
  }

  @Test
  void editProduct_WithExistingProductName_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("teszt jegy 1", 12000,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/2")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProductDetails)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product name already exists."));
  }

  @Test
  void editProduct_WithMissingPrice_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("1 week pass", null,
        "7 days", "Use this pass for a whole week!", 1L);

    mvc.perform(patch("/api/products/1")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProductDetails)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Price is missing"));
  }

  @Test
  void editProduct_WithInvalidProductType_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    ProductWithoutIdDTO newProductDetails = new ProductWithoutIdDTO("1 week pass", 9999,
        "7 days", "Use this pass for a whole week!", 99L);

    mvc.perform(patch("/api/products/1")
            .header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(newProductDetails)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product type is wrong."));
  }

  private String login(LoginUserDTO loginUserDTO) throws Exception {
    String responseContent = mvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.token").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> map = objectMapper.readValue(responseContent, new TypeReference<>() {
    });
    return map.get("token");
  }
}
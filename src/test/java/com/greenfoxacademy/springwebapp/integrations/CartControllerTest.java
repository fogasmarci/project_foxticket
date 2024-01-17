package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.security.JwtValidatorService;
import com.greenfoxacademy.springwebapp.services.CartService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class CartControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  CartService cartService;
  ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  JwtValidatorService jwtValidatorService;

  @Test
  void addProductToCart_WithValidProductId_ReturnsCorrectJson() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(2L);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.cart").value(hasSize(1)))
        .andExpect(jsonPath("$['cart'][0]['product_id']").value(productIdDTO.getProductId()))
        .andExpect(jsonPath("$['cart'][0]['name']").value("teszt bérlet 1"))
        .andExpect(jsonPath("$['cart'][0]['price']").value(4000));
  }

  @Test
  void addProductToCart_WithInvalidProductId_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(50L);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product doesn't exist."));
  }

  @Test
  void addProductToCart_WithMissingProductId_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(null);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product ID is required."));
  }

  @Test
  void listCartContents_WithEmptyCart_ReturnsCorrectJson() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);

    mvc.perform(get("/api/cart").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.cart").value(hasSize(0)));
  }

  @Test
  void listCartContents_WithProductsInCart_ReturnsCorrectJson() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("cica@cartuser.ab", "cica1234");
    String jwt = login(loginUserDTO);

    mvc.perform(get("/api/cart").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.cart").value(hasSize(3)));
  }

  @Test
  void addProductToCart_WithValidProductId_AndAmount_ReturnsCartContentCorrectly() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, 3);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.cart").value(hasSize(3)))
        .andExpectAll(jsonPath("$['cart'][0]['product_id']").value(productIdDTO.getProductId()),
            jsonPath("$['cart'][0]['name']").value("teszt bérlet 1"),
            jsonPath("$['cart'][0]['price']").value(4000))
        .andExpectAll(jsonPath("$['cart'][2]['product_id']").value(productIdDTO.getProductId()),
            jsonPath("$['cart'][0]['name']").value("teszt bérlet 1"),
            jsonPath("$['cart'][0]['price']").value(4000));
  }

  @Test
  void addProductToCart_WithInValidProductId_AndValidAmount_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(50L, 3);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product doesn't exist."));
  }

  @Test
  void addProductToCart_WithMissingProductId_AndValidAmount_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(null, 3);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product ID is required."));
  }

  @Test
  void addProductToCart_WithValidProductId_AndNegativeAmount_DoesNotAddAnythingToCart() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, -3);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Amount must be greater than 0."));
  }

  @Test
  void addProductToCart_WithValidProductId_AndZeroAmount_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, 0);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Amount must be greater than 0."));
  }

  @Test
  void addProductToCart_WithValidProductId_AndAmountOverLimit_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    ProductIdDTO productIdDTO = new ProductIdDTO(2L, 52);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Selected items cannot be added to cart. Cart limit is 50."));

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

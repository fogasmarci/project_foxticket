package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.ProductIdDTO;
import com.greenfoxacademy.springwebapp.security.JwtValidatorService;
import com.greenfoxacademy.springwebapp.services.CartService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
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
    String jwt = login();
    Claims claims = jwtValidatorService.parseAndValidateJwtToken(jwt);
    Long userId = ((Integer) claims.get("userId")).longValue();
    ProductIdDTO productIdDTO = new ProductIdDTO(2L);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(userId))
        .andExpect(jsonPath("$.productId").value(productIdDTO.getProductId()));
  }

  @Test
  void addProductToCart_WithInvalidProductId_ReturnsCorrectErrorMessage() throws Exception {
    String jwt = login();
    ProductIdDTO productIdDTO = new ProductIdDTO(50L);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product doesn't exist."));
  }

  @Test
  void addProductToCart_WithMissingProductId_ReturnsCorrectErrorMessage() throws Exception {
    String jwt = login();
    ProductIdDTO productIdDTO = new ProductIdDTO(null);

    mvc.perform(post("/api/cart").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(productIdDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product ID is required."));
  }

  private String login() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String responseContent = mvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.token").exists())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Map<String, String> map = objectMapper.readValue(responseContent, new TypeReference<>() {});
    return map.get("token");
  }
}

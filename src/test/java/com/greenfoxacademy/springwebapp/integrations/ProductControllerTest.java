package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.services.ProductService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class ProductControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  ProductService productService;
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void getProductDetails_ListsAllProducts() throws Exception {
    String jwt = login();
    mvc.perform(get("/api/products").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.products", isA(ArrayList.class)))
        .andExpect(jsonPath("$.products").value(hasSize(3)))
        .andExpect(jsonPath("$.products[0].name").value("teszt jegy 1"))
        .andExpect(jsonPath("$.products[1].description").value("teszt2"));
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

    Map<String, String> map = objectMapper.readValue(responseContent, Map.class);
    return map.get("token");
  }
}
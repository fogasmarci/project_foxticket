package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.NameDTO;
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
public class ProductTypeRestControllerTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;

  @Test
  void addProductType_NotLoggedIn_ReturnsUnauthorized() throws Exception {
    NameDTO nameDTO = new NameDTO("pass");
    mockMvc.perform(post("/api/product-types")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nameDTO)))
        .andExpect(status().is(302));
  }

  @Test
  void addProductType_WithLoggedUser_ReturnsForbidden() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    NameDTO nameDTO = new NameDTO("pass");

    mockMvc.perform(post("/api/product-types").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nameDTO)))
        .andExpect(status().is(403));
  }

  @Test
  void addProductType_WithLoggedAdminAndNullTitle_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    NameDTO nameDTO = new NameDTO("");

    mockMvc.perform(post("/api/product-types").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nameDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Name is required"));
  }

  @Test
  void addProductType_WithLoggedAdminAndExistingTitle_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    NameDTO nameDTO = new NameDTO("jegy");

    mockMvc.perform(post("/api/product-types").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nameDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Product type name already exists"));
  }

  @Test
  void addProductType_WithLoggedAdminAndValidInput_ReturnsNewProductType() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("admin@admin.admin", "password");
    String jwt = login(loginUserDTO);
    NameDTO nameDTO = new NameDTO("test pass");

    mockMvc.perform(post("/api/product-types").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(nameDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(3))
        .andExpect(jsonPath("$.name").value("test pass"));
  }

  private String login(LoginUserDTO loginUserDTO) throws Exception {
    String responseContent = mockMvc.perform(post("/api/users/login")
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

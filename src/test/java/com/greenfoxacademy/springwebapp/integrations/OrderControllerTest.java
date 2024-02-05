package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.services.OrderService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class OrderControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  OrderService orderService;
  @Autowired
  ObjectMapper objectMapper;

  @Test
  void listAllPurchasedItems_WithNoItemsBought_ReturnsEmptyList() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);

    mvc.perform(get("/api/orders").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['orders']").value(hasSize(0)));
  }

  @Test
  void listAllPurchasedItems_With4ItemsBought_ReturnsCorrectList() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("something@orderuser.xy", "rainbow1");
    String jwt = login(loginUserDTO);

    String response = mvc.perform(get("/api/orders").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['orders']").value(hasSize(4)))
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertTrue(response.contains("\"product_id\":1"));
    assertTrue(response.contains("\"product_id\":2"));
    assertTrue(response.contains("\"product_id\":3"));
  }

  @Test
  void activatePurchasedItem_WithValidOrderItemId_ItemIsActivated() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("something@orderuser.xy", "rainbow1");
    String jwt = login(loginUserDTO);

    String response = mvc.perform(patch("/api/orders/1").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$['status']").value("Active"))
        .andReturn()
        .getResponse()
        .getContentAsString();

    assertTrue(response.contains("\"id\":1"));
  }

  @Test
  void activatePurchasedItem_WithInValidOrderItemId_ThrowsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("something@orderuser.xy", "rainbow1");
    String jwt = login(loginUserDTO);

    mvc.perform(patch("/api/orders/111").header("Authorization", "Bearer " + jwt))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$['error']").value("This order does not belong to the user."));
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

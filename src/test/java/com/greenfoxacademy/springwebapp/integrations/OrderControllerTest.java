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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class OrderControllerTest {
  @Autowired
  MockMvc mvc;
  @Autowired
  OrderService orderService;
  ObjectMapper objectMapper = new ObjectMapper();

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

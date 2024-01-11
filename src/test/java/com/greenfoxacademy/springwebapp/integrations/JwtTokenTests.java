package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class JwtTokenTests {
  @Autowired
  MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void callingAuthenticatedEndpoint_WithNoToken_ShouldFail() throws Exception {
    mockMvc.perform(get("/api/products"))
        .andExpect(status().is(401));
  }

  @Test
  public void callingAuthenticatedEndpoint_WithInvalidToken_ShouldFail() throws Exception {
    String jwtToken = "not valid token";
    mockMvc.perform(get("/api/products")
            .header("Authorization", "Bearer " + jwtToken)
        )
        .andExpect(status().is(401));
  }

  @Test
  public void callingAuthenticatedEndpoint_WithGoodToken_ShouldSucceed() throws Exception {
    String jwtToken = login();
    mockMvc.perform(get("/api/products")
            .header("Authorization", "Bearer " + jwtToken)
        )
        .andExpect(status().is(200));
  }

  private String login() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String responseContent = mockMvc.perform(post("/api/users/login")
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

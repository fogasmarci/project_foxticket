package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class UserRestControllerTest {
  @Autowired
  MockMvc mockMvc;
  ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void manageRegistrationRequests_WithAlreadyTakenEmail_ReturnsCorrectErrorMessage() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("User1", "admin@admin.admin", "password");
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Email is already taken."));
  }

  @Test
  public void manageRegistrationRequests_WithNoEmail_ReturnsCorrectErrorMessage() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("User1", null, "password");
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Email is required."));
  }

  @Test
  public void manageRegistrationRequests_WithGoodInput_ReturnsResponseJson() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("ExampleUser", "user@example.com", "password");
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(jsonPath("$.email").value("user@example.com"))
        .andExpect(jsonPath("$.isAdmin").value(false));
  }

  @Test
  public void manageRegistrationRequests_WithBadInput_ReturnsCorrectErrorMessage() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(null, null, null);
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("All fields are required."));
  }

  @Test
  public void manageRegistrationRequests_WithNoPassword_ReturnsCorrectErrorMessage() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("User1", "user@user.user", null);
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Password is required."));
  }

  @Test
  public void manageRegistrationRequests_WithNoName_ReturnsCorrectErrorMessage() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(null, "user@user.user", "password");
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Name is required."));
  }

  @Test
  public void loginUser_WithNoPassword_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("example@example.com", null);
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Password is required."));
  }

  @Test
  public void loginUser_WithNoEmail_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO(null, "password");
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Email is required."));
  }

  @Test
  public void loginUser_WithNoInput_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO(null, null);
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("All fields are required."));
  }

  @Test
  public void loginUser_WithIncorrectPassword_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "123456789");
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Email or password is incorrect."));
  }

  @Test
  public void loginUser_WithIncorrectEmail_ReturnsCorrectErrorMessage() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.userrr", "12345678");
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Email or password is incorrect."));
  }

  @Test
  public void loginUser_WithGoodInput_ReturnsJwtToken() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    mockMvc.perform(post("/api/users/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(loginUserDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.status").value("ok"))
        .andExpect(jsonPath("$.token").exists());
  }
}
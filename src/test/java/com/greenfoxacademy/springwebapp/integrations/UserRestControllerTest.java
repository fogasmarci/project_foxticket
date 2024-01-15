package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserInfoRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
  public void manageRegistrationRequests_WithShortPassword_ReturnsCorrectErrorMessage() throws Exception {
    RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("User1", "user@user.user", "1234");
    mockMvc.perform(post("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Password must be at least 8 characters."));
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

  @Test
  public void updateUser_WithNullInput_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO(null, null, null);
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Name, email or Password is required"));
  }

//  @Override
//  public UserInfoResponseDTO updateUser(UserInfoRequestDTO updateDTO) {
//    String name = updateDTO.getName();
//    String email = updateDTO.getEmail();
//    String password = updateDTO.getPassword();
//    if (name == null && email == null && password == null) {
//      throw new FieldsException("Name, email or Password is required");
//    }
//    if (email != null && password != null) {
//      throw new PasswordEmailUpdateException();
//    }
//
//    User user = findLoggedInUser();
//    if (name != null && name.length() > 3) {
//      user.setName(name);
//    }
//    if (email != null && email.contains("@") && email.length() > 3) {
//      user.setEmail(email);
//    }
//    if (password != null && password.length() > 7) {
//      user.setPassword(password);
//    }
//
//    return updateUserInfoResponse(user);
//  }

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
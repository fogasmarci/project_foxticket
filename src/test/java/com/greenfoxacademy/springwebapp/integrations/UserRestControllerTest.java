package com.greenfoxacademy.springwebapp.integrations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserInfoRequestDTO;
import com.greenfoxacademy.springwebapp.models.VerificationEmail;
import com.greenfoxacademy.springwebapp.services.EmailServiceImpl;
import com.greenfoxacademy.springwebapp.services.UserServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class UserRestControllerTest {
  @Autowired
  MockMvc mockMvc;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  EmailServiceImpl emailService;
  @Autowired
  @InjectMocks
  UserServiceImpl userService;

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
    Mockito.when(emailService.sendSimpleMail(Mockito.any(VerificationEmail.class))).thenReturn(null);
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
  void updateUser_NotLoggedIn_ReturnsUnauthorized() throws Exception {
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("newName", null, null);
    mockMvc.perform(patch("/api/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(401));
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

  @Test
  public void updateUser_WithEmailAndPassword_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO(null, "test@testemail.com", "testtest");
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Cannot change password and email at the same time."));
  }

  @Test
  public void updateUser_WithInvalidEmail_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("newname", "testtestemail.com", null);
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Invalid email input."));
  }

  @Test
  public void updateUser_WithAlreadyTakenEmail_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("newname", "admin@admin.admin", null);
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Email is already taken."));
  }

  @Test
  public void updateUser_WithShortName_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("a", null, "newPassword");
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Name must be at least 3 letters long"));
  }

  @Test
  public void updateUser_WithShortEmail_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("newName", "@", null);
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Invalid email input."));
  }

  @Test
  public void updateUser_WithShortPassword_ReturnsCorrectError() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("newName", null, "short");
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(400))
        .andExpect(jsonPath("$.error").value("Password must be at least 8 characters."));
  }

  @Test
  public void updateUser_WithPasswordChange_ReturnsUpdatedInfo() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO(null, null, "newPassword");
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("TestUser"))
        .andExpect(jsonPath("$.email").value("user@user.user"));
    LoginUserDTO newLogin = new LoginUserDTO("user@user.user", "newPassword");
    login(newLogin);
  }

  @Test
  public void updateUser_WithNameChange_ReturnsUpdatedInfo() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO("newName", null, null);
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("newName"))
        .andExpect(jsonPath("$.email").value("user@user.user"));
  }

  @Test
  public void updateUser_WithEmailChange_ReturnsUpdatedInfo() throws Exception {
    LoginUserDTO loginUserDTO = new LoginUserDTO("user@user.user", "12345678");
    String jwt = login(loginUserDTO);
    UserInfoRequestDTO requestDTO = new UserInfoRequestDTO(null, "new@new.com", null);
    mockMvc.perform(patch("/api/users").header("Authorization", "Bearer " + jwt)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDTO)))
        .andExpect(status().is(200))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("TestUser"))
        .andExpect(jsonPath("$.email").value("new@new.com"));
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
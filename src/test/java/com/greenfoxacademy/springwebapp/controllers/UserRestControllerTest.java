package com.greenfoxacademy.springwebapp.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class UserRestControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void manageRegistrationRequests_WithBadInput_ReturnsCorrectErrorMessage() throws Exception {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO(null, null, null);
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error").value("Name, email and password are required."));

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
    public void manageRegistrationRequests_WithNoEmail_ReturnsCorrectErrorMessage() throws Exception {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("User1", null, "password");
        given(userService.createUser("User1", null, "password"))
                .willThrow(new IllegalArgumentException("Email is required."));
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error").value("Email is required."));

    }

    @Test
    public void manageRegistrationRequests_WithAlreadyTakenEmail_ReturnsCorrectErrorMessage() throws Exception {
        RegistrationRequestDTO requestDTO = new RegistrationRequestDTO("User1", "user@user.user", "password");
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error").value("Email is already taken."));

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
}
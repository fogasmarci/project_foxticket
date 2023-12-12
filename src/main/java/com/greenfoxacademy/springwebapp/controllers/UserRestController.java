package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.services.ErrorService;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UserRestController {
  private final UserService userService;
  private final ErrorService errorService;

  public UserRestController(UserService userService, ErrorService errorService) {
    this.userService = userService;
    this.errorService = errorService;
  }

  @PostMapping(path = "/users")
  public ResponseEntity<?> manageRegistrationRequests(@RequestBody RegistrationRequestDTO requestDTO) {
    if (!userService.isRegistrationRequestValid(requestDTO)) {
      return ResponseEntity.status(400).body(errorService.createRegistrationErrorMessage(requestDTO));
    }
    User newUser = userService.createUser(requestDTO.getName(), requestDTO.getEmail(), requestDTO.getPassword());
    return ResponseEntity.status(200).body(userService.createRegistrationDTO(newUser));
  }
}
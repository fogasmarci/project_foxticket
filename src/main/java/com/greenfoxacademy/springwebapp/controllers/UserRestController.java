package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;
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
    try {
      return ResponseEntity.status(200).body(userService.createRegistrationDTO(userService.registrateUser(requestDTO)));
    } catch (MissingFieldsException | RegistrationException e) {
      return ResponseEntity.badRequest().body(errorService.createErrorMessage(e.getMessage()));
    }
  }
}
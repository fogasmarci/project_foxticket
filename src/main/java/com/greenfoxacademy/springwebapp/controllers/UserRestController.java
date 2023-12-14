package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;
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

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(path = "/users")
  public ResponseEntity<?> registrateUser(@RequestBody RegistrationRequestDTO requestDTO) {
    try {
      return ResponseEntity.status(200).body(userService.createRegistrationDTO(userService.registrateUser(requestDTO)));
    } catch (MissingFieldsException | RegistrationException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
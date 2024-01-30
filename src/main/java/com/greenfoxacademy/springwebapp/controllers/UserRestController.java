package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserInfoRequestDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.login.LoginException;
import com.greenfoxacademy.springwebapp.exceptions.registration.EmailAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {
  private final UserService userService;

  public UserRestController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/api/users")
  public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDTO requestDTO) {
    try {
      return ResponseEntity.status(200).body(userService.registerUser(requestDTO));
    } catch (FieldsException | RegistrationException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }


  @PostMapping("/api/users/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginUserDTO loginUserDTO) {
    try {
      return ResponseEntity.status(200).body(userService.loginUser(loginUserDTO));
    } catch (FieldsException | LoginException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @PatchMapping("/api/users")
  public ResponseEntity<?> updateUserInformation(@RequestBody UserInfoRequestDTO updateDTO) {
    try {
      return ResponseEntity.status(200).body(userService.updateUser(updateDTO));
    } catch (FieldsException | EmailAlreadyTakenException | UsernameNotFoundException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
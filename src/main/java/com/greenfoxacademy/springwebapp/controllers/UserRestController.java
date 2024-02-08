package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.http.ResponseEntity;
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
  public ResponseEntity<RegistrationResponseDTO> registerUser(@RequestBody RegistrationRequestDTO requestDTO) {
    return ResponseEntity.status(200).body(userService.registerUser(requestDTO));
  }

  @PostMapping("/api/users/login")
  public ResponseEntity<LoginResponseDTO> loginUser(@RequestBody LoginUserDTO loginUserDTO) {
    return ResponseEntity.status(200).body(userService.loginUser(loginUserDTO));
  }

  @PatchMapping("/api/users")
  public ResponseEntity<UserInfoResponseDTO> updateUserInformation(@RequestBody UserInfoRequestDTO updateDTO) {
    return ResponseEntity.status(200).body(userService.updateUser(updateDTO));
  }
}
package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


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

  @PostMapping("/api/users/photo")
  public ResponseEntity<MessageDTO> uploadPhoto(@RequestParam("file") MultipartFile file) throws IOException {
    return ResponseEntity.status(200).body(userService.uploadPhoto(file));
  }
}
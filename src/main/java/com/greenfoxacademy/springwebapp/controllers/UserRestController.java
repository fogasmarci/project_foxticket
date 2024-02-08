package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.UserInfoRequestDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.login.LoginException;
import com.greenfoxacademy.springwebapp.exceptions.registration.EmailAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;
import com.greenfoxacademy.springwebapp.exceptions.user.MaxUploadSizeException;
import com.greenfoxacademy.springwebapp.exceptions.user.NotSupportedFileUploadException;
import com.greenfoxacademy.springwebapp.exceptions.verificationemail.FailedToSendEmailException;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
  public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDTO requestDTO) {
    try {
      return ResponseEntity.status(200).body(userService.registerUser(requestDTO));
    } catch (FieldsException | RegistrationException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    } catch (FailedToSendEmailException e) {
      return ResponseEntity.status(500).body(new ErrorMessageDTO(e.getMessage()));
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

  @PostMapping("/api/users/photo")
  public ResponseEntity<?> uploadPhoto(@RequestParam("file") MultipartFile file) {
    try {
      return ResponseEntity.status(200).body(userService.uploadPhoto(file));
    } catch (IOException e) {
      return ResponseEntity.status(500).body(new ErrorMessageDTO(e.getMessage()));
    } catch (UsernameNotFoundException e) {
      return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
    } catch (MaxUploadSizeException e) {
      return ResponseEntity.status(413).body(new ErrorMessageDTO(e.getMessage()));
    } catch (NotSupportedFileUploadException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.MissingFieldsException;
import com.greenfoxacademy.springwebapp.exceptions.login.LoginException;
import com.greenfoxacademy.springwebapp.exceptions.registration.RegistrationException;
import com.greenfoxacademy.springwebapp.security.JwtBuilder;
import com.greenfoxacademy.springwebapp.services.JpaUserDetailsService;
import com.greenfoxacademy.springwebapp.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class UserRestController {
  private final UserService userService;

  public UserRestController(UserService userService, JpaUserDetailsService userDetailsService, JwtBuilder jwtBuilder, AuthenticationManager authenticationManager) {
    this.userService = userService;
  }

  @PostMapping(path = "/users")
  public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDTO requestDTO) {
    try {
      return ResponseEntity.status(200).body(userService.createRegistrationDTO(userService.registerUser(requestDTO)));
    } catch (MissingFieldsException | RegistrationException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @PostMapping(path = "/users/login")
  public ResponseEntity<?> loginUser(@RequestBody LoginUserDTO loginUserDTO) throws Exception {
    try {
      return ResponseEntity.status(200).body(userService.loginUser(loginUserDTO, userService.createLoginResponse(loginUserDTO)));
    } catch (MissingFieldsException | LoginException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}
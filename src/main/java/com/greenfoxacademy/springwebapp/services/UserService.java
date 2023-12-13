package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationResponseDTO;
import com.greenfoxacademy.springwebapp.models.User;

public interface UserService {
  User getUserByEmail(String email);

  User createUser(String name, String email, String password);

  RegistrationResponseDTO createRegistrationDTO(User user);

  User registrateUser(RegistrationRequestDTO requestDTO);
}

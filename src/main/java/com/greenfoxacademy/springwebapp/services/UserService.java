package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.User;

public interface UserService {

  User createUser(String name, String email, String password);

  RegistrationResponseDTO createRegistrationDTO(User user);

  User registerUser(RegistrationRequestDTO requestDTO);

  LoginResponseDTO loginUser(LoginUserDTO loginUserDTO);

  User findLoggedInUser();

  Long findLoggedInUsersId();

  UserInfoResponseDTO updateUser(UserInfoRequestDTO updateDTO);
}

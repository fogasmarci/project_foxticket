package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.User;

public interface UserService {

  User createUser(String name, String email, String password);

  RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO);

  LoginResponseDTO loginUser(LoginUserDTO loginUserDTO);

  Long findLoggedInUsersId();

  UserInfoResponseDTO updateUser(UserInfoRequestDTO updateDTO);

  User getCurrentUser();
}

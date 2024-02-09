package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
  User createUser(String name, String email, String password);

  RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO);

  LoginResponseDTO loginUser(LoginUserDTO loginUserDTO);

  Long findLoggedInUsersId();

  UserInfoResponseDTO updateUser(UserInfoRequestDTO updateDTO);

  User getCurrentUser();

  MessageDTO uploadPhoto(MultipartFile file) throws IOException;

  User getUserById(Long userId);

  void saveUser(User user);
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;

public interface ErrorService {
  ErrorMessageDTO createErrorMessage(String errorMessage);

  ErrorMessageDTO createRegistrationErrorMessage(RegistrationRequestDTO requestDTO);

  ErrorMessageDTO createLoginErrorMessage(LoginUserDTO loginUserDTO);
}

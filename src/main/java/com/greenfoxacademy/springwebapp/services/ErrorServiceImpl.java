package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ErrorServiceImpl implements ErrorService {
  private final UserRepository userRepository;

  public ErrorServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public ErrorMessageDTO createErrorMessage(String errorMessage) {
    return new ErrorMessageDTO(errorMessage);
  }
}

package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;


public interface ErrorService {
  ErrorMessageDTO createErrorMessage(String errorMessage);
}

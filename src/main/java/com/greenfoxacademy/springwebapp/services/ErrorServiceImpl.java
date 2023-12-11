package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
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

    @Override
    public ErrorMessageDTO createRegistrationErrorMessage(RegistrationRequestDTO requestDTO) {
        if (requestDTO.getPassword() == null && requestDTO.getName() == null && requestDTO.getEmail() == null) {
            return new ErrorMessageDTO("Name, email and password are required.");
        }
        if (requestDTO.getPassword() == null) {
            return new ErrorMessageDTO("Password is required");
        }
        if (requestDTO.getName() == null) {
            return new ErrorMessageDTO("Name is required");
        }
        if (requestDTO.getEmail() == null) {
            return new ErrorMessageDTO("Email is required");
        }
        if (requestDTO.getPassword().length() < 8) {
            return new ErrorMessageDTO("Password must be at least 8 characters.");
        }
        if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            return new ErrorMessageDTO("Email is already taken.");
        }
        return new ErrorMessageDTO("Bad request.");
    }
}

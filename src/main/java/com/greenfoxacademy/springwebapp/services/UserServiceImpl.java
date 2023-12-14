package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.AllFieldsMissingException;
import com.greenfoxacademy.springwebapp.exceptions.fields.EmailRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.fields.NameRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.fields.PasswordRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.registration.EmailAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.registration.ShortPasswordException;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElse(null);
  }

  @Override
  public User createUser(String name, String email, String password) {
    return userRepository.save(new User(name, email, passwordEncoder.encode(password)));
  }

  @Override
  public RegistrationResponseDTO createRegistrationDTO(User user) {
    boolean isAdmin = user.getRoles().contains("ADMIN");
    return new RegistrationResponseDTO(user.getId(), user.getEmail(), isAdmin);
  }

  @Override
  public User registrateUser(RegistrationRequestDTO requestDTO) {
    if (requestDTO.getPassword() == null && requestDTO.getName() == null && requestDTO.getEmail() == null) {
      throw new AllFieldsMissingException();
    }
    if (requestDTO.getPassword() == null) {
      throw new PasswordRequiredException();
    }
    if (requestDTO.getName() == null) {
      throw new NameRequiredException();
    }
    if (requestDTO.getEmail() == null) {
      throw new EmailRequiredException();
    }
    if (requestDTO.getPassword().length() < 8) {
      throw new ShortPasswordException();
    }
    if (userRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
      throw new EmailAlreadyTakenException();
    }
    return userRepository.save(new User(requestDTO.getName(), requestDTO.getEmail(), requestDTO.getPassword()));
  }
}

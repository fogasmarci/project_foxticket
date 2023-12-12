package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
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
  public boolean isRegistrationRequestValid(RegistrationRequestDTO requestDTO) {
    User user = userRepository.findByEmail(requestDTO.getEmail()).orElse(null);
    return requestDTO.getName() != null && requestDTO.getPassword() != null && requestDTO.getPassword().length() > 7 && user == null;
  }
}

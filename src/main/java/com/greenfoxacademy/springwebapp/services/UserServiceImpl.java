package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.LoginResponseDTO;
import com.greenfoxacademy.springwebapp.dtos.LoginUserDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationRequestDTO;
import com.greenfoxacademy.springwebapp.dtos.RegistrationResponseDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.AllFieldsMissingException;
import com.greenfoxacademy.springwebapp.exceptions.fields.EmailRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.fields.NameRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.fields.PasswordRequiredException;
import com.greenfoxacademy.springwebapp.exceptions.login.IncorrectCredentialsException;
import com.greenfoxacademy.springwebapp.exceptions.registration.EmailAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.registration.ShortPasswordException;
import com.greenfoxacademy.springwebapp.models.SecurityUser;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import com.greenfoxacademy.springwebapp.security.JwtBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtBuilder jwtBuilder;
  private final JpaUserDetailsService userDetailsService;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtBuilder jwtBuilder, JpaUserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtBuilder = jwtBuilder;
    this.userDetailsService = userDetailsService;
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
  public User registerUser(RegistrationRequestDTO requestDTO) {
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
    return createUser(requestDTO.getName(), requestDTO.getEmail(), requestDTO.getPassword());
  }

  @Override
  public LoginResponseDTO loginUser(LoginUserDTO loginUserDTO) {
    if (loginUserDTO.getEmail() == null && loginUserDTO.getPassword() == null) {
      throw new AllFieldsMissingException();
    }
    if (loginUserDTO.getPassword() == null) {
      throw new PasswordRequiredException();
    }
    if (loginUserDTO.getEmail() == null) {
      throw new EmailRequiredException();
    }
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.getEmail(), loginUserDTO.getPassword()));
    } catch (BadCredentialsException e) {
      throw new IncorrectCredentialsException(e);
    }

    final SecurityUser userDetails = userDetailsService.loadUserByUsername(loginUserDTO.getEmail());
    String token = jwtBuilder.generateToken(userDetails);

    return new LoginResponseDTO(token);
  }

  @Override
  public User findLoggedInUser() {
    SecurityContext context = SecurityContextHolder.getContext();
    SecurityUser securityUser = (SecurityUser) context.getAuthentication().getPrincipal();
    return securityUser.getUser();
  }

  @Override
  public Long findLoggedInUsersId() {
    SecurityContext context = SecurityContextHolder.getContext();
    SecurityUser securityUser = (SecurityUser) context.getAuthentication().getPrincipal();
    return securityUser.getId();
  }
}

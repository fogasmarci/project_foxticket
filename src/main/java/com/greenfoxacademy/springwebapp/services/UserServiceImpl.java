package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.exceptions.fields.*;
import com.greenfoxacademy.springwebapp.exceptions.login.IncorrectCredentialsException;
import com.greenfoxacademy.springwebapp.exceptions.registration.EmailAlreadyTakenException;
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

import java.util.function.Consumer;

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
    validatePassword(requestDTO.getPassword());
    validateEmail(requestDTO.getEmail());
    validateName(requestDTO.getName());
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
    validatePassword(loginUserDTO.getPassword());
    validateEmail(loginUserDTO.getEmail());
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

  @Override
  public UserInfoResponseDTO updateUser(UserInfoRequestDTO updateDTO) {
    String name = updateDTO.getName();
    String email = updateDTO.getEmail();
    String password = updateDTO.getPassword();
    if (name == null && email == null && password == null) {
      throw new FieldsException("Name, email or Password is required");
    }
    if (email != null && password != null) {
      throw new PasswordEmailUpdateException();
    }
    if (email != null && !email.contains("@")) {
      throw new InvalidEmailException();
    }
    if (userRepository.findByEmail(email).orElse(null) != null) {
      throw new EmailAlreadyTakenException();
    }

    User user = userRepository.findById(findLoggedInUsersId()).orElse(null);
    setIfValid(name, 3, user::setName, new ShortNameException());
    setIfValid(email, 4, user::setEmail, new InvalidEmailException());
    setIfValid(password, 8, user::setPassword, new ShortPasswordException());
    userRepository.save(user);

    return updateUserInfoResponse(user);
  }

  private void setIfValid(String validate, int length, Consumer<String> setter, FieldsException invalidException) {
    if (validate != null && validate.length() < length) {
      throw invalidException;
    }
    if (validate != null) {
      setter.accept(validate);
    }
  }

  private UserInfoResponseDTO updateUserInfoResponse(User user) {
    return new UserInfoResponseDTO(user.getId(), user.getName(), user.getEmail());
  }

  private void validateName(String name) {
    if (name == null) {
      throw new NameRequiredException();
    }
    if (name.length() < 4) {
      throw new ShortNameException();
    }
  }

  private void validateEmail(String email) {
    if (email == null) {
      throw new EmailRequiredException();
    }
    if (!email.contains("@") && email.length() < 4) {
      throw new InvalidEmailException();
    }
  }

  private void validatePassword(String password) {
    if (password == null) {
      throw new PasswordRequiredException();
    }
    if (password.length() < 8) {
      throw new ShortPasswordException();
    }
  }
}

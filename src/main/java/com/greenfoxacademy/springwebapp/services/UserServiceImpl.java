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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {
  private static final int emailMinLength = 3;
  private static final int nameMinLength = 3;
  private static final int passwordMinLength = 8;
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
    validateField(requestDTO.getName(), nameMinLength, new ShortNameException(), new NameRequiredException());
    validateField(requestDTO.getEmail(), emailMinLength, new InvalidEmailException(), new EmailRequiredException());
    validateField(requestDTO.getPassword(), passwordMinLength, new ShortPasswordException(), new PasswordRequiredException());
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
    validateField(loginUserDTO.getEmail(), emailMinLength, new InvalidEmailException(), new EmailRequiredException());
    validateField(loginUserDTO.getPassword(), passwordMinLength, new ShortPasswordException(), new PasswordRequiredException());
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

    User user = getCurrentUser();
    validateMinLength(name, nameMinLength, new ShortNameException());
    validateMinLength(email, emailMinLength, new InvalidEmailException());
    validateMinLength(password, passwordMinLength, new ShortPasswordException());
    setField(name, user::setName);
    setField(email, user::setEmail);
    setField(password, user::setPassword);
    userRepository.save(user);

    return updateUserInfoResponse(user);
  }

  @Override
  public User getCurrentUser() {
    return userRepository.findById(findLoggedInUsersId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  private UserInfoResponseDTO updateUserInfoResponse(User user) {
    return new UserInfoResponseDTO(user.getId(), user.getName(), user.getEmail());
  }

  private void validateField(String validate, int length, FieldsException invalidException, FieldsException nullException) {
    if (validate == null) {
      throw nullException;
    }
    validateMinLength(validate, length, invalidException);
  }

  private void validateMinLength(String validate, int length, FieldsException invalidException) {
    if (validate != null && validate.length() < length) {
      throw invalidException;
    }
  }

  private void setField(String value, Consumer<String> setter) {
    if (value != null) {
      setter.accept(value);
    }
  }
}

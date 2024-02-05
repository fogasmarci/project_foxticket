package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.*;
import com.greenfoxacademy.springwebapp.exceptions.fields.*;
import com.greenfoxacademy.springwebapp.exceptions.login.IncorrectCredentialsException;
import com.greenfoxacademy.springwebapp.exceptions.registration.EmailAlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.user.InvalidUserIdException;
import com.greenfoxacademy.springwebapp.models.*;
import com.greenfoxacademy.springwebapp.repositories.RoleRepository;
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
  private final RoleRepository roleRepository;
  private final EmailService emailService;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtBuilder jwtBuilder, JpaUserDetailsService userDetailsService, RoleRepository roleRepository, EmailService emailService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtBuilder = jwtBuilder;
    this.userDetailsService = userDetailsService;
    this.roleRepository = roleRepository;
    this.emailService = emailService;
  }

  @Override
  public User createUser(String name, String email, String password) {
    User user = new User(name, email, encodePassword(password));
    user.addRole(roleRepository.findByAuthority(Authorities.USER).get());
    return userRepository.save(user);
  }

  @Override
  public RegistrationResponseDTO registerUser(RegistrationRequestDTO requestDTO) {
    if (requestDTO.password() == null && requestDTO.name() == null && requestDTO.email() == null) {
      throw new AllFieldsMissingException();
    }
    validateField(requestDTO.name(), nameMinLength, new ShortNameException(), new NameRequiredException());
    validateField(requestDTO.email(), emailMinLength, new InvalidEmailException(), new EmailRequiredException());
    validateField(requestDTO.password(), passwordMinLength, new ShortPasswordException(), new PasswordRequiredException());
    if (userRepository.existsByEmail(requestDTO.email())) {
      throw new EmailAlreadyTakenException();
    }
    User newUser = createUser(requestDTO.name(), requestDTO.email(), requestDTO.password());
    sendVerificationEmail(newUser);
    return createRegistrationDTO(newUser);
  }

  @Override
  public LoginResponseDTO loginUser(LoginUserDTO loginUserDTO) {
    if (loginUserDTO.email() == null && loginUserDTO.password() == null) {
      throw new AllFieldsMissingException();
    }
    validateField(loginUserDTO.email(), emailMinLength, new InvalidEmailException(), new EmailRequiredException());
    validateField(loginUserDTO.password(), passwordMinLength, new ShortPasswordException(), new PasswordRequiredException());
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUserDTO.email(), loginUserDTO.password()));
    } catch (BadCredentialsException e) {
      throw new IncorrectCredentialsException(e);
    }

    final SecurityUser userDetails = userDetailsService.loadUserByUsername(loginUserDTO.email());
    String token = jwtBuilder.generateToken(userDetails);

    return new LoginResponseDTO(token);
  }

  @Override
  public Long findLoggedInUsersId() {
    SecurityContext context = SecurityContextHolder.getContext();
    SecurityUser securityUser = (SecurityUser) context.getAuthentication().getPrincipal();
    return securityUser.getId();
  }

  @Override
  public UserInfoResponseDTO updateUser(UserInfoRequestDTO updateDTO) {
    String name = updateDTO.name();
    String email = updateDTO.email();
    String password = updateDTO.password();

    if (name == null && email == null && password == null) {
      throw new FieldsException("Name, email or Password is required");
    }
    if (email != null && password != null) {
      throw new PasswordEmailUpdateException();
    }
    if (email != null && !email.contains("@")) {
      throw new InvalidEmailException();
    }
    if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyTakenException();
    }
    validateMinLength(name, nameMinLength, new ShortNameException());
    validateMinLength(email, emailMinLength, new InvalidEmailException());
    validateMinLength(password, passwordMinLength, new ShortPasswordException());
    if (password != null) {
      password = encodePassword(password);
    }

    User user = getCurrentUser();
    setField(name, user::setName);
    setField(email, user::setEmail);
    setField(password, user::setPassword);
    if (email != null) {
      if (user.getIsVerified()) {
        VerificationToken token = new VerificationToken(user);
        user.setVerificationToken(token);
        user.setIsVerified(false);
      }

      sendVerificationEmail(user);
    }

    userRepository.save(user);
    return updateUserInfoResponse(user);
  }

  @Override
  public User getCurrentUser() {
    return userRepository.findById(findLoggedInUsersId()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
  }

  @Override
  public User getUserById(Long userId) {
    return userRepository.findById(userId).orElseThrow(InvalidUserIdException::new);
  }

  @Override
  public void saveUser(User user) {
    userRepository.save(user);
  }

  private String encodePassword(String password) {
    return passwordEncoder.encode(password);
  }

  private RegistrationResponseDTO createRegistrationDTO(User user) {
    boolean isAdmin = user.getIsAdminByRoles();
    return new RegistrationResponseDTO(user.getId(), user.getEmail(), isAdmin, "Verification e-mail sent.");
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

  private void sendVerificationEmail(User registeredUser) {
    String verificationLink = String.format("http://localhost:8080/email-verification/%s?token=%s",
        registeredUser.getId(), registeredUser.getVerificationToken().getToken());
    emailService.sendSimpleMail(new VerificationEmail(registeredUser.getEmail(), verificationLink, registeredUser.getName()));
  }
}

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

@Service
public class UserServiceImpl implements UserService {
  private static final int EMAIL_MIN_LENGTH = 3;
  private static final int NAME_MIN_LENGTH = 3;
  private static final int PASSWORD_MIN_LENGTH = 8;
  private static final int MAX_LENGTH = 50;
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
    validateRegisterDto(requestDTO);
    User newUser = createUser(requestDTO.name(), requestDTO.email(), requestDTO.password());
    sendVerificationEmail(newUser);
    return createRegistrationDTO(newUser);
  }


  @Override
  public LoginResponseDTO loginUser(LoginUserDTO loginUserDTO) {
    validateLoginDto(loginUserDTO);

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

    validateUpdateDto(name, email, password);

    User user = getCurrentUser();

    if (name != null) {
      user.setName(name);
    }
    if (password != null) {
      password = encodePassword(password);
      user.setPassword(password);
    }
    if (email != null) {
      user.setEmail(email);
      handleEmailVerification(user);
    }

    userRepository.save(user);
    return updateUserInfoResponse(user);
  }

  private void handleEmailVerification(User user) {
    if (user.getIsVerified()) {
      VerificationToken token = new VerificationToken(user);
      user.setVerificationToken(token);
      user.setIsVerified(false);
    }
    sendVerificationEmail(user);
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
    return new RegistrationResponseDTO(user.getId(), user.getEmail(), isAdmin);
  }

  private UserInfoResponseDTO updateUserInfoResponse(User user) {
    return new UserInfoResponseDTO(user.getId(), user.getName(), user.getEmail());
  }

  private void validateRegisterDto(RegistrationRequestDTO requestDTO) {
    if (requestDTO.password() == null && requestDTO.name() == null && requestDTO.email() == null) {
      throw new AllFieldsMissingException();
    }
    validateField(requestDTO.name(), new NameRequiredException(), this::validateName);
    validateField(requestDTO.email(), new EmailRequiredException(), this::validateEmail);
    validateField(requestDTO.password(), new PasswordRequiredException(), this::validatePassword);
    if (userRepository.existsByEmail(requestDTO.email())) {
      throw new EmailAlreadyTakenException();
    }
  }

  private void validateLoginDto(LoginUserDTO loginUserDTO) {
    if (loginUserDTO.email() == null && loginUserDTO.password() == null) {
      throw new AllFieldsMissingException();
    }
    validateField(loginUserDTO.email(), new EmailRequiredException(), this::validateEmail);
    validateField(loginUserDTO.password(), new PasswordRequiredException(), this::validatePassword);
  }

  private void validateUpdateDto(String name, String email, String password) {
    if (name == null && email == null && password == null) {
      throw new FieldsException("Name, email or Password is required");
    }
    if (email != null && password != null) {
      throw new PasswordEmailUpdateException();
    }
    validatePassword(password);
    validateName(name);
    validateEmail(email);
    if (userRepository.existsByEmail(email)) {
      throw new EmailAlreadyTakenException();
    }
  }

  private void validateField(String validate, FieldsException nullException, ValidateFunction validator) {
    if (validate == null) {
      throw nullException;
    }
    validator.validate(validate);
  }

  private void validateEmail(String email) {
    if (email != null) {
      if (!email.contains("@") || email.length() < EMAIL_MIN_LENGTH) {
        throw new InvalidEmailException();
      }
      if (email.length() > MAX_LENGTH) {
        throw new LongUserFieldException();
      }
    }
  }

  private void validateName(String name) {
    if (name != null) {
      if (name.length() < NAME_MIN_LENGTH) {
        throw new ShortNameException();
      }
      if (name.length() > MAX_LENGTH) {
        throw new LongUserFieldException();
      }
    }
  }

  private void validatePassword(String password) {
    if (password != null) {
      if (password.length() < PASSWORD_MIN_LENGTH) {
        throw new ShortPasswordException();
      }
      if (password.length() > MAX_LENGTH) {
        throw new LongUserFieldException();
      }
    }
  }

  private void sendVerificationEmail(User registeredUser) {
    String verificationLink = String.format("http://localhost:8080/email-verification/%s?token=%s",
        registeredUser.getId(), registeredUser.getVerificationToken().getToken());
    emailService.sendSimpleMail(new VerificationEmail(registeredUser.getEmail(), verificationLink, registeredUser.getName()));
  }
}

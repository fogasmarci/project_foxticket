package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.models.SecurityUser;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public JpaUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public SecurityUser loadUserByUsername(String email) throws UsernameNotFoundException {
    return userRepository
        .findByEmail(email)
        .map(SecurityUser::new) // create a new instance of SecurityUser using the constructor of the SecurityUser class
        .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));
  }
}
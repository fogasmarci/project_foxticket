package com.greenfoxacademy.springwebapp;

import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BasicSpringProjectApplication implements CommandLineRunner {

  private final UserRepository userRepository;

  @Autowired
  public BasicSpringProjectApplication(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public static void main(String[] args) {
    SpringApplication.run(BasicSpringProjectApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    userRepository.save(new User("Admin", "admin@admin.com", "adminadmin", "ROLE_USER,ROLE_ADMIN"));
  }
}

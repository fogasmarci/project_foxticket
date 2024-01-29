package com.greenfoxacademy.springwebapp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
  public UserController() {

  }

  @GetMapping("/")
  public String displayMainPage() {
    return "main-page";
  }

  @GetMapping("/register")
  public String displayRegisterForm() {
    return "register-page";
  }

  @GetMapping("/login")
  public String displayLoginForm() {
    return "login";
  }

  @GetMapping("/error")
  public String displayErrorPage() {
    return "error";
  }
}

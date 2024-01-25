package com.greenfoxacademy.springwebapp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
  public UserController() {

  }

  @GetMapping(path = "/")
  public String displayMainPage() {
    return "main-page";
  }

  @GetMapping(path = "/register")
  public String displayRegisterForm() {
    return "register-page";
  }

  @GetMapping(path = "/login")
  public String displayLoginForm() {
    return "login";
  }
}

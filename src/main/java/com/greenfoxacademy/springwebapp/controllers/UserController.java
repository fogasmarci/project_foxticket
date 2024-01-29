package com.greenfoxacademy.springwebapp.controllers;


import com.greenfoxacademy.springwebapp.dtos.ArticleListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
  public UserController() {}

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

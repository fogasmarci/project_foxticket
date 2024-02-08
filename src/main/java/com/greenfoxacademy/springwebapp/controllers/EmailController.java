package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.services.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class EmailController {
  private final EmailService emailService;

  @Autowired
  public EmailController(EmailService emailService) {
    this.emailService = emailService;
  }

  @PostMapping("/api/email-verification/{userId}")
  public ResponseEntity<?> sendVerificationMail(@PathVariable Long userId) {
    return ResponseEntity.status(200).body(emailService.resendVerificationMail(userId));
  }

  @PatchMapping("/api/email-verification/{userId}")
  public ResponseEntity<?> verifyUser(@PathVariable Long userId, @RequestParam(name = "token") String verificationToken) {
    return ResponseEntity.status(200).body(emailService.verifyUser(userId, verificationToken));
  }
}

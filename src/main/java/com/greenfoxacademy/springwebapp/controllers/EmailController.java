package com.greenfoxacademy.springwebapp.controllers;

import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.user.InvalidUserIdException;
import com.greenfoxacademy.springwebapp.exceptions.user.InvalidVerificationTokenException;
import com.greenfoxacademy.springwebapp.exceptions.verificationemail.FailedToSendEmailException;
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
    try {
      return ResponseEntity.status(200).body(emailService.resendVerificationMail(userId));
    } catch (FailedToSendEmailException e) {
      return ResponseEntity.status(500).body(new ErrorMessageDTO(e.getMessage()));
    } catch (InvalidUserIdException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }

  @PatchMapping("/api/email-verification/{userId}")
  public ResponseEntity<?> verifyUser(@PathVariable Long userId, @RequestParam(name = "token") String verificationToken) {
    try {
      return ResponseEntity.status(200).body(emailService.verifyUser(userId, verificationToken));
    } catch (InvalidUserIdException | InvalidVerificationTokenException e) {
      return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
    }
  }
}

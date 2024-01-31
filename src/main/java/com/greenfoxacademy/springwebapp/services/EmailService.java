package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.models.VerificationEmail;

public interface EmailService {

  MessageDTO sendSimpleMail(VerificationEmail details);

  MessageDTO resendVerificationMail(Long userId);

  MessageDTO verifyUser(Long userId, String verificationToken);
}

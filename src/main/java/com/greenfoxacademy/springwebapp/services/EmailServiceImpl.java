package com.greenfoxacademy.springwebapp.services;

import com.greenfoxacademy.springwebapp.dtos.MessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.user.InvalidVerificationTokenException;
import com.greenfoxacademy.springwebapp.exceptions.verificationemail.FailedToSendEmailException;
import com.greenfoxacademy.springwebapp.models.User;
import com.greenfoxacademy.springwebapp.models.VerificationEmail;
import com.greenfoxacademy.springwebapp.models.VerificationToken;
import com.greenfoxacademy.springwebapp.repositories.VerificationTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
  private final JavaMailSender javaMailSender;
  @Value("${spring.mail.username}")
  private String sender;
  private final UserService userService;
  private final VerificationTokenRepository tokenRepository;

  @Autowired
  public EmailServiceImpl(JavaMailSender javaMailSender, @Lazy UserService userService, VerificationTokenRepository tokenRepository) {
    this.javaMailSender = javaMailSender;
    this.userService = userService;
    this.tokenRepository = tokenRepository;
  }

  @Override
  public MessageDTO sendSimpleMail(VerificationEmail details) {
    try {
      SimpleMailMessage mailMessage
          = new SimpleMailMessage();

      mailMessage.setFrom(sender);
      mailMessage.setTo(details.getRecipient());
      mailMessage.setText(details.getMsgBody());
      mailMessage.setSubject(details.getSubject());

      javaMailSender.send(mailMessage);
      return new MessageDTO("Mail Sent Successfully...");
    } catch (MailAuthenticationException e) {
      throw new FailedToSendEmailException();
    }
  }

  @Override
  public MessageDTO resendVerificationMail(Long userId) {
    User user = userService.getUserById(userId);
    String verificationLink = String.format("http://localhost:8080/email-verification/%s?token=%s",
        userId, user.getVerificationToken().getToken());
    return sendSimpleMail(new VerificationEmail(user.getEmail(), verificationLink, user.getName()));
  }

  @Override
  @Transactional
  public MessageDTO verifyUser(Long userId, String verificationToken) {
    User user = userService.getUserById(userId);
    VerificationToken token = user.getVerificationToken();
    if (token.getToken().equals(verificationToken)) {
      user.setIsVerified(true);
      user.setVerificationToken(null);
      tokenRepository.delete(token);
      userService.saveUser(user);
      return new MessageDTO("User is successfully verified.");
    }

    throw new InvalidVerificationTokenException();
  }
}

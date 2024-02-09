package com.greenfoxacademy.springwebapp.controllers;

import com.google.zxing.WriterException;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.notfound.ResourceNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.server.FailedToSendEmailException;
import com.greenfoxacademy.springwebapp.exceptions.taken.AlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.unauthorized.IncorrectCredentialsException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(FieldsException.class)
  public ResponseEntity<ErrorMessageDTO> handleFieldException(FieldsException e) {
    return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(IncorrectCredentialsException.class)
  public ResponseEntity<ErrorMessageDTO> handleIncorrectCredentialsException(IncorrectCredentialsException e) {
    return ResponseEntity.status(401).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorMessageDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
    return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorMessageDTO> handleUsernameNotFoundException(UsernameNotFoundException e) {
    return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(AlreadyTakenException.class)
  public ResponseEntity<ErrorMessageDTO> handleAlreadyTakenException(AlreadyTakenException e) {
    return ResponseEntity.status(409).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(WriterException.class)
  public ResponseEntity<ErrorMessageDTO> handleWriterException() {
    return ResponseEntity.status(500).body(new ErrorMessageDTO("QR code creation failed"));
  }

  @ExceptionHandler(IOException.class)
  public ResponseEntity<ErrorMessageDTO> handleIOException() {
    return ResponseEntity.status(500).body(new ErrorMessageDTO("QR code creation failed"));
  }

  @ExceptionHandler(FailedToSendEmailException.class)
  public ResponseEntity<ErrorMessageDTO> handleFailedToSendEmailException(FailedToSendEmailException e) {
    return ResponseEntity.status(500).body(new ErrorMessageDTO(e.getMessage()));
  }
}



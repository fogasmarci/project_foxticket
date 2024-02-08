package com.greenfoxacademy.springwebapp.globalexceptionhandling;

import com.google.zxing.WriterException;
import com.greenfoxacademy.springwebapp.dtos.ErrorMessageDTO;
import com.greenfoxacademy.springwebapp.exceptions.fields.FieldsException;
import com.greenfoxacademy.springwebapp.exceptions.notfound.ResourceNotFoundException;
import com.greenfoxacademy.springwebapp.exceptions.other.AlreadyActiveException;
import com.greenfoxacademy.springwebapp.exceptions.taken.AlreadyTakenException;
import com.greenfoxacademy.springwebapp.exceptions.user.InvalidVerificationTokenException;
import com.greenfoxacademy.springwebapp.exceptions.verificationemail.FailedToSendEmailException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(FieldsException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ResponseEntity<ErrorMessageDTO> handleFieldException(FieldsException e) {
    return ResponseEntity.status(400).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorMessageDTO> handleResourceNotFoundException(ResourceNotFoundException e) {
    return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(AlreadyTakenException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorMessageDTO> handleAlreadyTakenException(AlreadyTakenException e) {
    return ResponseEntity.status(409).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(UsernameNotFoundException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorMessageDTO> handleAuthenticationException(UsernameNotFoundException e) {
    return ResponseEntity.status(404).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(AlreadyActiveException.class)
  @ResponseStatus(HttpStatus.CONFLICT)
  public ResponseEntity<ErrorMessageDTO> handleAlreadyActiveException(AlreadyActiveException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(WriterException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorMessageDTO> handleWriterException() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO("QR code creation failed"));
  }

  @ExceptionHandler(IOException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorMessageDTO> handleIOException() {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO("QR code creation failed"));
  }

  @ExceptionHandler(FailedToSendEmailException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ErrorMessageDTO> handleFailedToSendEmailException(FailedToSendEmailException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
  }

  @ExceptionHandler(InvalidVerificationTokenException.class)
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public ResponseEntity<ErrorMessageDTO> handleInvalidVerificationTokenException(InvalidVerificationTokenException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessageDTO(e.getMessage()));
  }
}



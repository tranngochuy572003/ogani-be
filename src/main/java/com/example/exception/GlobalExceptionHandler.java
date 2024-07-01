package com.example.exception;

import com.example.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = RuntimeException.class)
  ResponseEntity<ErrorResponse> handlingRuntimeException(RuntimeException e) {
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(),HttpStatus.NOT_IMPLEMENTED.value(), e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(errorResponse);
  }
  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(value = NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException e) {
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(), e.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
  }

  @ExceptionHandler(value = ForbiddenException.class)
  public ResponseEntity<ErrorResponse> handleTokenRefreshException(ForbiddenException e) {
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.FORBIDDEN.value(), e.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
  }
}

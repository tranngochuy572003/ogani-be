package com.example.exception;

import com.example.api.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody

public class GlobalExceptionHandler {
  @ExceptionHandler(value = RuntimeException.class)
  ResponseEntity<ErrorResponse> handlingRuntimeException(RuntimeException e) {
    ErrorResponse errorResponse = new com.example.api.ErrorResponse(LocalDateTime.now(),HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
  @ExceptionHandler(value = BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException e) {
    ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }
}

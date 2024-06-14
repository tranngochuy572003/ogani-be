package com.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(value = RuntimeException.class)
  ResponseEntity<String> handlingRuntimeException (RuntimeException e){
    return  ResponseEntity.badRequest().body(e.getMessage());
  }
}

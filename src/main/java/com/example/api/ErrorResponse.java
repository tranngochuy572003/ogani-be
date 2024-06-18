package com.example.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ErrorResponse {
  private LocalDateTime timestamp;
  private int statusCode;
  private String message;
}

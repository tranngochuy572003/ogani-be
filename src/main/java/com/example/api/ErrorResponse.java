package com.example.api;

import java.time.LocalDateTime;
public class ErrorResponse {
  private LocalDateTime timestamp;
  private int statusCode;
  private String message;

  public ErrorResponse(LocalDateTime timestamp, int statusCode, String message) {
    this.timestamp = timestamp;
    this.statusCode = statusCode;
    this.message = message;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}

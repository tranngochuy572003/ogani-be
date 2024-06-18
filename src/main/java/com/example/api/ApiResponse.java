package com.example.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
  private int statusCode;
  private String message;
  private Object data;

  public ApiResponse(int statusCode, Object data) {
    this.statusCode = statusCode;
    this.data = data;
    this.message="Successfully";
  }

  public ApiResponse(int statusCode) {
    this.statusCode = statusCode;
    this.data = "No contents";
    this.message="Successfully";
  }
}

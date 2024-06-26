package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationDto {
  private String userName;
  private String password;
  private String fullName;
  private String address;
  private String phoneNumber;
  private String role;
}

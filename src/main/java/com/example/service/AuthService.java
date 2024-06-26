package com.example.service;

import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;

public interface AuthService {
  public boolean checkPassword(String email, String rawPassword);
  public String isAuthenticated(AuthenticationDto authenticationDto);
  void register(RegisterDto registerDto);
}

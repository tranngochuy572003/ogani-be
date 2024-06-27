package com.example.service;

import com.example.dto.RegisterDto;

public interface AuthService {
  public boolean checkPassword(String email, String rawPassword);
  public String isAuthenticated(String email,String password);
  void register(RegisterDto registerDto);
}

package com.example.service;

import com.example.dto.AuthenticationDto;

public interface AuthService {
  public boolean checkPassword(String email, String rawPassword);
  public String isAuthenticated(AuthenticationDto authenticationDto);
}

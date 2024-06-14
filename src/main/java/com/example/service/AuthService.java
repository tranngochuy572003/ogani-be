package com.example.service;

import com.example.dto.UserDtoLogin;

public interface AuthService {
  public boolean checkPassword(String email, String rawPassword);
  public String isAuthenticated(UserDtoLogin userDtoLogin);
}

package com.example.service;

import com.example.api.JwtResponse;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;

import java.text.ParseException;
public interface AuthService {
  JwtResponse login(AuthenticationDto authenticationDto) throws ParseException;
  String isAuthenticated(String email,String rawPassword);
  void register(RegisterDto registerDto);
}

package com.example.service;

import com.example.dto.AuthorizationDto;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;

import java.text.ParseException;
public interface AuthService {
  AuthorizationDto login(AuthenticationDto authenticationDto) throws ParseException;
  String createTokenByValidAccount(String email,String rawPassword);
  void register(RegisterDto registerDto);
  void logout(String authorizationHeader);

}

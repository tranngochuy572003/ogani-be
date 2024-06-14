package com.example.service.impl;

import com.example.dto.UserDtoLogin;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.common.MessageConstant.EMAIL_PASSWORD_INVALID;
import static com.example.common.MessageConstant.UNAUTHORIZED;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private com.example.securityconfig.JwtTokenProvider jwtTokenProvider;


  public boolean checkPassword(String email, String rawPassword) {

    try {
      User user = userRepository.findUserByEmail(email);
      if (user != null) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
      }

    }catch (Exception e){
      System.out.println(e.getMessage());
    }

    return false;
  }

  public String isAuthenticated(UserDtoLogin userDtoLogin) {
    boolean isAuthenticated = checkPassword(userDtoLogin.getUserName(), userDtoLogin.getPassword());
    try {
      if (isAuthenticated) {
        return jwtTokenProvider.createToken(userDtoLogin);
      }else {
        throw new BadRequestException(EMAIL_PASSWORD_INVALID);
      }
    } catch (Exception e) {
      throw new BadRequestException(UNAUTHORIZED);
    }
  }}

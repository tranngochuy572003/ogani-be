package com.example.service.impl;

import com.example.config.JwtTokenProvider;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.common.MessageConstant.*;

@Service
public class AuthServiceImpl implements AuthService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private UserService userService;
  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private JwtTokenProvider jwtTokenProvider;


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

  public String isAuthenticated(AuthenticationDto authenticationDto) {
    boolean isAuthenticated = checkPassword(authenticationDto.getUserName(), authenticationDto.getPassword());
    try {
      if (isAuthenticated) {
        return jwtTokenProvider.createToken(authenticationDto);
      }else {
        throw new BadRequestException(EMAIL_PASSWORD_INVALID);
      }
    } catch (Exception e) {
      throw new BadRequestException(UNAUTHORIZED);
    }
  }

  @Override
  public void register(RegisterDto registerDto) {
    if(userService.existsByUsername(registerDto.getUserName())){
      if(!registerDto.getConfirmPassword().equals(registerDto.getPassword())){
        throw new BadRequestException(CONFIRM_PASSWORD_INCORRECT);
      }
      UserDto userDto = UserMapper.toUserDto(registerDto);
      userService.addUser(userDto);
    }

  }
}

package com.example.service.impl;

import com.example.api.AuthorizationDto;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.AuthService;
import com.example.service.JwtTokenService;
import com.example.service.UserService;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.JWTParser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.common.MessageConstant.*;

@Service
public class AuthServiceImpl implements AuthService {
  private UserRepository userRepository;
  private UserService userService;
  private PasswordEncoder passwordEncoder;
  private JwtTokenService jwtTokenService;


  public AuthServiceImpl(UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder, JwtTokenService jwtTokenService) {
    this.userRepository = userRepository;
    this.userService = userService;
    this.passwordEncoder = passwordEncoder;
    this.jwtTokenService = jwtTokenService;
  }


  public void register(RegisterDto registerDto) {
    if(userService.existsByUsername(registerDto.getUserName())){
      if(!registerDto.getConfirmPassword().equals(registerDto.getPassword())){
        throw new BadRequestException(CONFIRM_PASSWORD_INCORRECT);
      }
      UserDto userDto = UserMapper.toUserDto(registerDto);
      userService.addUser(userDto);
    }

  }

  @Override
  public AuthorizationDto login(AuthenticationDto authenticationDto)  {
    try {
      UserDetails userDetails = userService.loadUserByUsername(authenticationDto.getUserName());
      String jwtToken = isAuthenticated(authenticationDto.getUserName(), authenticationDto.getPassword());

      List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
              .collect(Collectors.toList());

      String refreshToken = jwtTokenService.createRefreshToken(jwtToken);
      JWT jwt = JWTParser.parse(refreshToken);
      JWTClaimsSet claimsSet = jwt.getJWTClaimsSet();
      String userName = (String) claimsSet.getClaim("userName");
      User user = userService.findUserByEmail(userName);
      user.setRefreshToken(refreshToken);
      userRepository.save(user);
      return new AuthorizationDto(jwtToken, refreshToken, user.getId(), user.getUsername(), user.isActive(), roles);
    }
    catch (ParseException e){
      throw new BadRequestException(FIELD_INVALID);

    }
  }

  @Override
  public String isAuthenticated(String email, String rawPassword) {
    try {
      User user = userRepository.findUserByEmail(email);
      if (user != null) {
        if (passwordEncoder.matches(rawPassword, user.getPassword())) {
          return jwtTokenService.createToken(email);
        } else {
          throw new BadRequestException(EMAIL_PASSWORD_INVALID);
        }
      } else {
        throw new BadRequestException(FIELD_INVALID);
      }
    } catch (Exception e) {
      throw new BadRequestException(UNAUTHORIZED);

    }
  }
}

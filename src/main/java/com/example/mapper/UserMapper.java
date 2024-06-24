package com.example.mapper;

import com.example.dto.UserDto;
import com.example.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

  public static UserDto toDto(User user,UserDto userDto) {
    userDto.setUserName(user.getUsername());
    userDto.setIsActive(user.isActive());
    userDto.setPassword(user.getPassword());
    userDto.setFullName(user.getFullName());
    userDto.setPhoneNumber(user.getPhoneNumber());
    userDto.setRole(user.getRole());
    userDto.setAddress(user.getAddress());

    return userDto;
  }

  public static User toCreateEntity(UserDto userDto) {
    User user = new User();
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setUserName(userDto.getUserName());
    user.setActive(userDto.getIsActive());
    user.setPassword(encoder.encode(userDto.getPassword()));
    user.setFullName(userDto.getFullName());
    user.setPhoneNumber(userDto.getPhoneNumber());
    user.setRole(userDto.getRole());
    user.setAddress(userDto.getAddress());
    return user;
  }

  public static User toUpdateEntity(User user ,UserDto userDto) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setUserName(userDto.getUserName());
    user.setActive(userDto.getIsActive());
    user.setPassword(encoder.encode(userDto.getPassword()));
    user.setFullName(userDto.getFullName());
    user.setPhoneNumber(userDto.getPhoneNumber());
    user.setRole(userDto.getRole());
    user.setAddress(userDto.getAddress());
    return user;
  }
}

package com.example.mapper;

import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

  public static UserDto toDto(User user,UserDto userDto) {
    userDto.setUserName(user.getUsername());
    userDto.setActive(user.isActive());
    userDto.setPassword(user.getPassword());
    userDto.setFullName(user.getFullName());
    userDto.setPhoneNumber(user.getPhoneNumber());
    userDto.setRole(user.getRole());
    userDto.setAddress(user.getAddress());

    return userDto;
  }

  public static User toEntity(User user ,UserDto userDto) {
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    user.setActive(userDto.isActive());
    user.setPassword(encoder.encode(userDto.getPassword()));
    user.setFullName(userDto.getFullName());
    user.setPhoneNumber(userDto.getPhoneNumber());
    user.setRole(userDto.getRole());
    user.setAddress(userDto.getAddress());
    return user;
  }
}

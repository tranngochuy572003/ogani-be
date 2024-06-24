package com.example.mapper;

import com.example.dto.UserDto;
import com.example.entity.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

  public static UserDto toDto(User user) {
    UserDto userDto = new UserDto();
    userDto.setUserName(user.getUsername());
    userDto.setActive(user.isActive());
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
    user.setActive(userDto.isActive());
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
    user.setActive(userDto.isActive());
    user.setPassword(encoder.encode(userDto.getPassword()));
    user.setFullName(userDto.getFullName());
    user.setPhoneNumber(userDto.getPhoneNumber());
    user.setRole(userDto.getRole());
    user.setAddress(userDto.getAddress());
    return user;
  }

  public static List<UserDto> toListDto(List<User> users) {
    List<UserDto> userDtoList  = new ArrayList<>();
    for(User user :users){
      userDtoList.add(UserMapper.toDto(user));
    }
    return userDtoList;
  }
}

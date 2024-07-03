package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
  void addUser(UserDto userDto);
  List<UserDto> getAllUsers();
  UserDto getUserById(String id) ;
  User findUserById(String id) ;
  void deleteUser(String id) ;
  void updateUser(String id , UserDto userDto) ;
  User findUserByEmail(String email);
  boolean existsByUsername(String username);
  UserDetails loadUserByUsername(String email);
  User getUserByRefreshToken(String token);
  void save(User user);
}

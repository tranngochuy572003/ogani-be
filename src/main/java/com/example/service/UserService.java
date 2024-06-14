package com.example.service;

import com.example.dto.UserDto;
import com.example.entity.User;

import java.util.List;

public interface UserService {
  void addUser(User user);
  List<User> getAllUsers();
  User getUserById(String id) ;
  void deleteUser(String id) ;
  void updateUser(String id , UserDto userDto) ;
  User findUserByEmail(String email);
  boolean checkUserExist(String username);
}

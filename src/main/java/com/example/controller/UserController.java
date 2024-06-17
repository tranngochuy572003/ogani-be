package com.example.controller;


import com.example.service.AuthService;
import com.example.service.impl.AuthServiceImpl;
import com.example.dto.UserDto;
import com.example.dto.UserDtoLogin;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.MessageConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @Autowired
  private AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<?> userLogin(@RequestBody UserDtoLogin userDtoLogin) {
    String jwtToken = authService.isAuthenticated(userDtoLogin);
    return new ResponseEntity<>(jwtToken, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
    if(userService.existsByUsername(userDto.getUserName())){
      userService.addUser(userDto);
    }
    return new ResponseEntity<>(ITEM_CREATED_SUCCESS, HttpStatus.CREATED);
  }

  @GetMapping
  public ResponseEntity<List<User>> userList() {
    List<User> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<User> getUser(@PathVariable String id) {
    User user = userService.getUserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
    userService.updateUser(id, userDto);
    return new ResponseEntity<>(ITEM_UPDATED_SUCCESS, HttpStatus.OK);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    return new ResponseEntity<>(ITEM_DELETED_SUCCESS, HttpStatus.OK);
  }

}

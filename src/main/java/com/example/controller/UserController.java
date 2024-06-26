package com.example.controller;


import com.example.api.ApiResponse;
import com.example.dto.UserDto;
import com.example.dto.UserDtoLogin;
import com.example.service.AuthService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.common.MessageConstant.*;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;


  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addUser(@RequestBody UserDto userDto) {
    if(userService.existsByUsername(userDto.getUserName())){
      userService.addUser(userDto);
    }
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_CREATED_SUCCESS);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping
  public ResponseEntity<ApiResponse> userList() {
    List<UserDto> users = userService.getAllUsers();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),users));
  }

  @GetMapping("/get/{id}")
  public ResponseEntity<ApiResponse> getUser(@PathVariable String id) {
    UserDto userDto = userService.getUserById(id);
    return ResponseEntity.ok(new ApiResponse (HttpStatus.OK.value(),userDto));
  }
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
    userService.updateUser(id, userDto);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_UPDATED_SUCCESS);
    return ResponseEntity.ok(response);
  }
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/delete/{id}")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable String id) {
    userService.deleteUser(id);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_DELETED_SUCCESS);
    return ResponseEntity.ok(response);
  }

}

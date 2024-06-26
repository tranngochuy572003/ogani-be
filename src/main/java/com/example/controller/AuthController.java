package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.AuthenticationDto;
import com.example.dto.UserDto;
import com.example.mapper.AuthenticationMapper;
import com.example.service.AuthService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.common.MessageConstant.REGISTER_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthenticationDto authenticationDto) {
        String jwtToken = authService.isAuthenticated(authenticationDto);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),jwtToken));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody AuthenticationDto authenticationDto) {
        if(userService.existsByUsername(authenticationDto.getUserName())){
            UserDto userDto = AuthenticationMapper.toDto(authenticationDto);
            userService.addUser(userDto);
        }
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(REGISTER_SUCCESS);
        return ResponseEntity.ok(response);
    }
}

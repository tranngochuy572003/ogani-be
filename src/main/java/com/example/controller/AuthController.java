package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.AuthenticationDto;
import com.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody AuthenticationDto authenticationDto) {
        String jwtToken = authService.isAuthenticated(authenticationDto);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),jwtToken));
    }
}

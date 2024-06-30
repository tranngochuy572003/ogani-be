package com.example.controller;

import com.example.api.ApiResponse;
import com.example.api.JwtResponse;
import com.example.api.TokenRefreshRequest;
import com.example.api.TokenRefreshResponse;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;
import com.example.exception.TokenRefreshException;
import com.example.service.AuthService;
import com.example.service.JwtTokenService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

import static com.example.common.MessageConstant.REGISTER_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtTokenService jwtTokenService;
    @Autowired
    UserService userService;


    @PostMapping("/login")
    public ApiResponse login(@RequestBody AuthenticationDto authenticationDto) throws ParseException {
        JwtResponse jwtResponse = authService.login(authenticationDto);
        return new ApiResponse(HttpStatus.OK.value(), jwtResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(REGISTER_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refresh(@RequestBody TokenRefreshRequest request) throws TokenRefreshException, ParseException {
        String refreshToken = jwtTokenService.createRefreshToken(request.getRefreshToken());
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), new TokenRefreshResponse(refreshToken)));

    }
}

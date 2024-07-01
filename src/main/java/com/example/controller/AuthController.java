package com.example.controller;

import com.example.api.ApiResponse;
import com.example.api.AuthorizationDto;
import com.example.api.TokenDto;
import com.example.api.TokenRefreshRequest;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;
import com.example.exception.ForbiddenException;
import com.example.service.AuthService;
import com.example.service.JwtTokenService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<ApiResponse> login(@RequestBody AuthenticationDto authenticationDto) throws ParseException {
        AuthorizationDto authorizationDto = authService.login(authenticationDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setData(authorizationDto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(REGISTER_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<ApiResponse> refreshToken(@RequestHeader ("Authorization") String authorizationHeader) throws ForbiddenException {
        String refreshToken = authorizationHeader.replace("Bearer ", "");
        String token = jwtTokenService.createRefreshToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(), new TokenDto(token)));

    }
}
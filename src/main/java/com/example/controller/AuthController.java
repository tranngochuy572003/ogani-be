package com.example.controller;

import com.example.api.ApiResponse;
import com.example.api.JwtResponse;
import com.example.api.TokenRefreshRequest;
import com.example.api.TokenRefreshResponse;
import com.example.config.*;
import com.example.dto.AuthenticationDto;
import com.example.dto.RegisterDto;
import com.example.entity.RefreshToken;
import com.example.entity.User;
import com.example.exception.TokenRefreshException;
import com.example.service.AuthService;
import com.example.service.impl.RefreshTokenServiceImpl;
import com.example.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.common.MessageConstant.REGISTER_SUCCESS;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    RefreshTokenServiceImpl refreshTokenServiceImpl;
    @Autowired
    UserServiceImpl userServiceImpl;


    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody AuthenticationDto authenticationDto) {
        UserDetails userDetails = userServiceImpl.loadUserByUsername(authenticationDto.getUserName());
        String jwtToken = authService.isAuthenticated(authenticationDto.getUserName(), authenticationDto.getPassword());
        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());
        try {
            RefreshToken refreshToken = refreshTokenServiceImpl.createRefreshToken(authenticationDto.getUserName());
            return ResponseEntity.ok(new JwtResponse(jwtToken, refreshToken.getToken(), roles));
        } catch (Exception e) {
            throw new TokenRefreshException("Token creation failed due to duplicate key.");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterDto registerDto) {
        authService.register(registerDto);
        ApiResponse response = new ApiResponse(HttpStatus.OK.value());
        response.setMessage(REGISTER_SUCCESS);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) throws TokenRefreshException {
        String requestRefreshToken = request.getRefreshToken();

        Optional<RefreshToken> refreshTokenOptional = refreshTokenServiceImpl.findByToken(requestRefreshToken);
        if (refreshTokenOptional.isEmpty()) {
            throw new TokenRefreshException("Refresh token is not in database!");
        }
        User user = refreshTokenServiceImpl.findUserByToken(requestRefreshToken);
        RefreshToken refreshToken =refreshTokenServiceImpl.verifyExpiration(refreshTokenOptional.get());

        String token = jwtTokenProvider.createToken(user.getUsername());

        return ResponseEntity.ok(new TokenRefreshResponse(token, refreshToken.getToken()));
    }
}

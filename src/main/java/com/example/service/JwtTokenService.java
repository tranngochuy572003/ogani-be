package com.example.service;

import com.example.exception.ForbiddenException;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;

public interface JwtTokenService {
    String createToken(String userName);
    String refreshToken(String userName);
    String createRefreshToken(String userName) throws ForbiddenException;
    boolean verifyExpiration(String token);
    boolean isValidToken(String token , UserDetails userDetails);
    String extractUserNameFromJWT(String token) throws ParseException;
    String hmacSha256(String data, String secret);
}

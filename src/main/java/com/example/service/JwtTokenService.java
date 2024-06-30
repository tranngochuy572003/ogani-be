package com.example.service;

import org.springframework.security.core.userdetails.UserDetails;

import java.text.ParseException;

public interface JwtTokenService {
    String createToken(String userName);
    String refreshToken(String userName);
    String createRefreshToken(String userName) throws ParseException;
    boolean verifyExpiration(String token) throws ParseException;
    boolean isValidToken(String token , UserDetails userDetails) throws ParseException;
    String extractUserNameFromJWT(String token) throws ParseException;
    String hmacSha256(String data, String secret);
}

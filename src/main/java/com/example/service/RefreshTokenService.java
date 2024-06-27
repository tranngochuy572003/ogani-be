package com.example.service;

import com.example.entity.RefreshToken;
import com.example.entity.User;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> findByToken(String token);
    User findUserByToken(String token);
    RefreshToken createRefreshToken(String userName);
    RefreshToken verifyExpiration(RefreshToken token);
}

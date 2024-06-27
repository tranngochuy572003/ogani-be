package com.example.service.impl;

import com.example.entity.RefreshToken;
import com.example.entity.User;
import com.example.exception.TokenRefreshException;
import com.example.repository.RefreshTokenRepository;
import com.example.repository.UserRepository;
import com.example.service.RefreshTokenService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@Data
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private Long refreshTokenDurationMs = 300000L  ;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    @Override
    public User findUserByToken(String token) {
        return refreshTokenRepository.findUserByToken(token);
    }
    @Override
    public RefreshToken createRefreshToken(String userName) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(userRepository.findUserByEmail(userName));
        refreshToken.setExpiryDate(Instant.now().plus(Duration.ofMinutes(5)));
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    @Override
    public RefreshToken verifyExpiration(RefreshToken token) throws TokenRefreshException {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired. Please make a new sign in request");
        }
        return token;
    }
}

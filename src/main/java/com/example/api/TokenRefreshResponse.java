package com.example.api;

import lombok.Data;

@Data
public class TokenRefreshResponse {
    private String token;

    public TokenRefreshResponse(String token) {
        this.token = token;
    }
}

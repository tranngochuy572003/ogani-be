package com.example.api;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private List<String> roles;

    public JwtResponse(String accessToken, String refreshToken, List<String> roles) {
        this.token = accessToken;
        this.refreshToken = refreshToken;
        this.roles = roles;
    }


}

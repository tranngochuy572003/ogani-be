package com.example.api;

import lombok.Data;

import java.util.List;

@Data
public class AuthorizationDto {
    private Token token;
    private User user;

    public AuthorizationDto(String accessToken, String refreshToken, String id, String username, boolean isActive, List<String> roles ) {
        this.token = new Token(accessToken, refreshToken);
        this.user = new User(id,username,isActive,roles);
    }

    @Data
    public static class Token {
        private String accessToken;
        private String refreshToken;

        public Token(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }

    @Data
    public static class User {
        private String id;
        private String username;
        private boolean isActive;
        private List<String> roles;
        public User(String id,String username, boolean isActive ,List<String> roles) {
            this.id = id;
            this.username = username;
            this.isActive = isActive;
            this.roles = roles;
        }
    }


}

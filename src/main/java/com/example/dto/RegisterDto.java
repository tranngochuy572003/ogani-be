package com.example.dto;

import lombok.Data;

@Data
public class RegisterDto {
    private String userName;
    private String password;
    private String confirmPassword ;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String role;
}

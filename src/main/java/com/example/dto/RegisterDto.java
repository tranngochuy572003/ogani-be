package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterDto {
    private String userName;
    private String password;
    private String confirmPassword ;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String role;
}

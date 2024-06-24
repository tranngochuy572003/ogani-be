package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data

@NoArgsConstructor
public class UserDto {
    private String userName;
    private String fullName;
    private String password;
    private String address;
    private String phoneNumber;
    private String role;
    private Boolean isActive;

    public UserDto(String userName, String fullName, String password, String address, String phoneNumber, String role, Boolean isActive) {
        this.userName = userName;
        this.fullName = fullName;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isActive = Objects.requireNonNullElse(isActive, true);
    }
}

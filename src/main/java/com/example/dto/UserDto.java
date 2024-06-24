package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userName;
    private String fullName;
    private String password;
    private String address;
    private String phoneNumber;
    private String role;
    @JsonProperty(value = "isActive")
    private boolean isActive;

}

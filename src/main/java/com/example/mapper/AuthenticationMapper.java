package com.example.mapper;

import com.example.dto.AuthenticationDto;
import com.example.dto.UserDto;

public class AuthenticationMapper {
    public static UserDto toDto(AuthenticationDto authenticationDto) {
        UserDto userDto = new UserDto();
        userDto.setUserName(authenticationDto.getUserName());
        userDto.setPassword(authenticationDto.getPassword());
        userDto.setFullName(authenticationDto.getFullName());
        userDto.setPhoneNumber(authenticationDto.getPhoneNumber());
        userDto.setAddress(authenticationDto.getAddress());
        return userDto;
    }
}

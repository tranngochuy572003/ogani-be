package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
  private String userName ;
  private String fullName ;
  private String password ;
  private String address;
  private String phoneNumber;
  private String role ;
  private Boolean isActive;
}

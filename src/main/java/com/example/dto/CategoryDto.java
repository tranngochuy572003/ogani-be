package com.example.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class CategoryDto {
  private String name ;
  private String type ;
  private Boolean isActive;


  public CategoryDto(String name, String type, Boolean isActive) {
    this.name = name;
    this.type = type;
    this.isActive = Objects.requireNonNullElse(isActive, true);
  }



}

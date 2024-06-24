package com.example.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class CategoryDto {
  private String name ;
  private String type ;
  @Builder.Default
  private Boolean isActive;
}

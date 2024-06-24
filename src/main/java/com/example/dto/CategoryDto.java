package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDto {
  private String name ;
  private String type ;
  @JsonProperty(value = "isActive")
  private boolean isActive;


}

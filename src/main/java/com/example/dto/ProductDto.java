package com.example.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
  private String nameProduct ;
  @JsonProperty(value = "isActive")
  private boolean isActive;
  private String  inventory;
  private String  description;
  private String  information;
  private String price ;
  private String category;
  private List<String> imageList;

}

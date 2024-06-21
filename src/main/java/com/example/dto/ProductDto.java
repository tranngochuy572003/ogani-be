package com.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
  private String nameProduct ;
  private boolean isActive;
  private Long  inventory;
  private String  description;
  private String  information;
  private Long price ;
  private List<String> imageList;

}

package com.example.dto;


import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ProductDto {
  private String nameProduct ;
  @Builder.Default
  private Boolean isActive;
  private Long  inventory;
  private String  description;
  private String  information;
  private Long price ;
  private String category;
  private List<String> imageList;

}

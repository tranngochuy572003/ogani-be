package com.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
  private String nameProduct ;
  private boolean isActive;
  private Long  inventory;
  private String  description;
  private String  availability;
  private String  shipping;
  private String  information;
  private String price ;

}

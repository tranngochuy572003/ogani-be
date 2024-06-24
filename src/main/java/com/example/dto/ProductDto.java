package com.example.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
public class ProductDto {
  private String nameProduct ;
  private Boolean isActive;
  private Long  inventory;
  private String  description;
  private String  information;
  private Long price ;
  private String category;
  private List<String> imageList;

  public ProductDto(String nameProduct, Boolean isActive, Long inventory, String description, String information, Long price, String category, List<String> imageList) {
    this.nameProduct = nameProduct;
    this.isActive = Objects.requireNonNullElse(isActive, true);
    this.inventory = inventory;
    this.description = description;
    this.information = information;
    this.price = price;
    this.category = category;
    this.imageList = imageList;
  }
}

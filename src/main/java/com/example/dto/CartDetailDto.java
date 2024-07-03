package com.example.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetailDto{
  @JsonProperty(value = "isChosen")
  private boolean isChosen;
  private Long quantityProduct;
  private String productId;
}

package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillDetailDto {
   private Long quantity ;
   private Long price;
   private String productName;
   private String urlImg;
}
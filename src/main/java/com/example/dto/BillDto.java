package com.example.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDto {
  private String billId;
  private String userId;
  private LocalDateTime createdDate;
  private Long totalAmount;
  List<BillDetailDto> billDetailDtoList;
}

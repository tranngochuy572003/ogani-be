package com.example.mapper;

import com.example.dto.BillDto;
import com.example.entity.Bill;

public class BillMapper {
  public static BillDto toDto(Bill bill) {
    BillDto billDto = new BillDto();
    billDto.setTax(bill.getTax());
    billDto.setTotalPrice(bill.getTotalPrice());

    return billDto;
  }

  public static Bill toEntity(BillDto billDto) {
    Bill bill = new Bill();
    bill.setTax(billDto.getTax());
    bill.setTotalPrice(billDto.getTotalPrice());
    return bill;
  }
}


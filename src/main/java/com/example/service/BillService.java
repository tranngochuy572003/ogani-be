package com.example.service;

import com.example.dto.BillDto;
import com.example.dto.OrderDto;
import com.example.entity.Bill;

import java.util.List;

public interface BillService {
    BillDto getBillById(String billId);
    Bill confirmOrder(OrderDto orderDto , String userId);
    List<BillDto> getBillByUserId(String userId);
}
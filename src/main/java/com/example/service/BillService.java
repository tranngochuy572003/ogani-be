package com.example.service;

import com.example.dto.BillDto;
import com.example.dto.OrderDto;
import com.example.entity.Bill;

public interface BillService {
    BillDto getBillById(String userId);
    Bill confirmOrder(OrderDto orderDto , String userId);
}
package com.example.service;

import com.example.dto.BillDto;

import java.util.List;

public interface BillService {
    BillDto getBillById(String billId);
    void confirmOrder(String billId);
    List<BillDto> getBillByUserId(String userId);
    void order(String userId);
    boolean isAuthorizedToGetBill(String billId,String userId);
}
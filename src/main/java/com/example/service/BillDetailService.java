package com.example.service;

import com.example.entity.BillDetail;

import java.util.List;

public interface BillDetailService {
    void save(BillDetail billDetail);
    List<BillDetail> findByBillsId(String billId);
}
package com.example.service.impl;

import com.example.entity.BillDetail;
import com.example.repository.BillDetailRepository;
import com.example.service.BillDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillDetailServiceImpl implements BillDetailService {
    @Autowired
    private BillDetailRepository billDetailRepository ;

    @Override
    public void save(BillDetail billDetail) {
        billDetailRepository.save(billDetail);
    }

    @Override
    public List<BillDetail> findByBillsId(String billId) {
        return billDetailRepository.findByBillsId(billId);
    }
}
package com.example.repository;

import com.example.entity.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillDetailRepository extends JpaRepository<BillDetail,String> {
    List<BillDetail> findByBillsId(String billId);
}
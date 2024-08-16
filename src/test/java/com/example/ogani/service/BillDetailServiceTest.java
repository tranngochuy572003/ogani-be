package com.example.ogani.service;

import com.example.entity.Bill;
import com.example.entity.BillDetail;
import com.example.repository.BillDetailRepository;
import com.example.service.impl.BillDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

class BillDetailServiceTest {
    @InjectMocks
    BillDetailServiceImpl billDetailService;
    private BillDetail billDetail;
    @Mock
    private BillDetailRepository billDetailRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Bill bill = new Bill();
        billDetail = new BillDetail("urlImg", "nameProduct", 100L, 2L, bill);

    }

    @Test
    void testSaveBillDetail(){
        billDetailService.save(billDetail);
        verify(billDetailRepository).save(billDetail);

    }
}

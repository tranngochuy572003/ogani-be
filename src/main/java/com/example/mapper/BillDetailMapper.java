package com.example.mapper;

import com.example.dto.BillDetailDto;
import com.example.entity.BillDetail;

public class BillDetailMapper {
    public static BillDetail toEntity(BillDetailDto billDetailDto) {
        BillDetail billDetail = new BillDetail();
        billDetail.setUrlImg(billDetailDto.getUrlImg());
        billDetail.setNameProduct(billDetailDto.getProductName());
        billDetail.setPrice(billDetailDto.getPrice()*billDetailDto.getQuantity());
        return billDetail;
    }
}
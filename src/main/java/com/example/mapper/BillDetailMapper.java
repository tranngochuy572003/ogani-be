package com.example.mapper;

import com.example.dto.BillDetailDto;
import com.example.entity.BillDetail;

import java.util.ArrayList;
import java.util.List;

public class BillDetailMapper {
    private BillDetailMapper() {
    }

    public static BillDetail toEntity(BillDetailDto billDetailDto) {
        BillDetail billDetail = new BillDetail();
        billDetail.setQuantity(billDetailDto.getQuantity());
        billDetail.setUrlImg(billDetailDto.getUrlImg());
        billDetail.setNameProduct(billDetailDto.getProductName());
        billDetail.setPrice(billDetailDto.getPrice()*billDetailDto.getQuantity());
        return billDetail;
    }

    public static BillDetailDto toDto(BillDetail billDetail) {
        BillDetailDto billDetailDto = new BillDetailDto();
        billDetailDto.setQuantity(billDetail.getQuantity());
        billDetailDto.setUrlImg(billDetail.getUrlImg());
        billDetailDto.setProductName(billDetail.getNameProduct());
        billDetailDto.setPrice(billDetail.getPrice());
        return billDetailDto;
    }


    public static List<BillDetail> toListEntity(List<BillDetailDto> billDetailDtoList) {
        List<BillDetail> billDetail  = new ArrayList<>();
        for(BillDetailDto billDetailDto :billDetailDtoList){
            billDetail.add(BillDetailMapper.toEntity(billDetailDto));
        }
        return billDetail;
    }
    public static List<BillDetailDto> toListDto(List<BillDetail> billDetailList) {
        List<BillDetailDto> billDetailDtoList  = new ArrayList<>();
        for(BillDetail billDetail :billDetailList){
            billDetailDtoList.add(BillDetailMapper.toDto(billDetail));
        }
        return billDetailDtoList;
    }
}
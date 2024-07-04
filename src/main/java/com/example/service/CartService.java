package com.example.service;

import com.example.dto.CartDetailDto;
import com.example.entity.CartDetail;

import java.util.List;


public interface CartService {
    void createCart(String userId, List<CartDetailDto> cartDetailDto);
    void updateCart(String cartId, List<CartDetailDto> cartDetailDto);
    List<String> getListProductIdFromCartDetailList(List<CartDetail> cartDetail);
    List<String> getListProductIdFromCartDetailDtoList(List<CartDetailDto> cartDetailDto);
}

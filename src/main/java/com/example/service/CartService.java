package com.example.service;

import com.example.dto.CartDetailDto;

import java.util.List;


public interface CartService {
    void createCart(String userId, List<CartDetailDto> cartDetailDto);
    void updateCart(String cartId, List<CartDetailDto> cartDetailDto);
}

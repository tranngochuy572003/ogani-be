package com.example.service;

import com.example.dto.CartDetailDto;
import com.example.dto.CartDto;
import com.example.entity.Cart;

import java.util.List;


public interface CartService {
    void createCart(String userId, List<CartDetailDto> cartDetailDto);
    void updateCart(String cartId, List<CartDetailDto> cartDetailDto);
    CartDto getByUserId(String userId);
    CartDto getByCartId(String cartId);
    void deleteCartById(String cartId);
    Cart getCartByUserId(String userId);
    void save(Cart cart);
}

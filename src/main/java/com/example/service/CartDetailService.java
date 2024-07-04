package com.example.service;

import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;

import java.util.List;


public interface CartDetailService {
    void deleteAllByCarts(Cart cart);
    List<CartDetail> findByCarts(Cart cart);
    CartDetail findByProducts(Product product);
    void save(CartDetail cartDetail);
    void deleteCartDetailByProductId(String productId);
}

package com.example.service.impl;

import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;
import com.example.repository.CartDetailRepository;
import com.example.service.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartDetailServiceImpl implements CartDetailService{
    @Autowired
    CartDetailRepository cartDetailRepository;
    @Override
    public void deleteAllByCarts(Cart cart) {
        cartDetailRepository.deleteAllByCarts(cart);
    }
    @Override
    public List<CartDetail> findByCarts(Cart cart) {
       return cartDetailRepository.findByCarts(cart);
    }

    @Override
    public void save(CartDetail cartDetail) {
        cartDetailRepository.save(cartDetail);
    }

    @Override
    public void deleteCartDetailByProductId(String productId) {
        cartDetailRepository.deleteCartDetailByProducts(productId);
    }
}

package com.example.service.impl;

import com.example.dto.CartDetailDto;
import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.CartDetailMapper;
import com.example.repository.CartRepository;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.common.MessageConstant.*;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    UserService userService ;
    @Autowired
    ProductService productService;
    @Autowired
    CartRepository cartRepository;

    @Override
    public void createCart(String userId, List<CartDetailDto> cartDetailDto) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUsers(user);
        if(user==null){
            throw new BadRequestException(VALUE_NO_EXIST);
        }

        if(cart==null){
            Cart newCart = new Cart();
            newCart.setId(UUID.randomUUID().toString());
            newCart.setUsers(user);

            List<CartDetail> cartDetailList = new ArrayList<>();
            for (CartDetailDto cartDetailInList : cartDetailDto) {
                if (cartDetailInList.getQuantityProduct() <=0 ) {
                    throw new BadRequestException(FIELD_INVALID);
                }

                CartDetail cartDetail  = CartDetailMapper.toEntity(cartDetailInList);
                Product product =productService.findProductById(cartDetailInList.getProductId());
                if(product==null){
                    throw new BadRequestException(VALUE_NO_EXIST);
                }
                cartDetail.setProducts(product);
                cartDetail.setCarts(newCart);
                cartDetailList.add(cartDetail);
            }
            newCart.setCartDetails(cartDetailList);
            cartRepository.save(newCart);
        }
        else {
            throw new BadRequestException(VALUE_EXISTED);
        }
    }


}

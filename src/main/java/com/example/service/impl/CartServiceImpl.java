package com.example.service.impl;

import com.example.dto.CartDetailDto;
import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.exception.BadRequestException;
import com.example.mapper.CartDetailMapper;
import com.example.repository.CartRepository;
import com.example.service.CartDetailService;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

import static com.example.common.MessageConstant.*;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    UserService userService;
    @Autowired
    ProductService productService;
    @Autowired
    CartRepository cartRepository;
    @Autowired
    CartDetailService cartDetailService;


    @Override
    public void createCart(String userId, List<CartDetailDto> cartDetailDto) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUsers(user);
        if(user==null){
            throw new BadRequestException(VALUE_NO_EXIST);
        }

        if(cart==null){
            Cart newCart = new Cart();
            newCart.setUsers(user);
            cartRepository.save(newCart);

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
    @Override
    public void updateCart(String cartId, List<CartDetailDto> cartDetailDtoList) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isPresent()) {
            if (cartDetailDtoList.isEmpty()) {
                cartDetailService.deleteAllByCarts(cart.get());
                return;
            }
            List<CartDetail> cartDetailList = cartDetailService.findByCarts(cart.get());
            Map<String, CartDetailDto> mapProductId = getMapCartDetailDto(cartDetailDtoList);
            for (CartDetail cartDetail : cartDetailList) {
                String productId = cartDetail.getProducts().getId();
                if (mapProductId.get(productId) == null) {
                    cartDetailService.deleteCartDetailByProductId(productId);
                } else {
                    CartDetailMapper.toUpdateEntity(cartDetail, mapProductId.get(productId));
                    cartDetailService.save(cartDetail);
                }
                mapProductId.remove(productId);
            }
            for (String productid : mapProductId.keySet()) {
                Product product = productService.findProductById(productid);
                CartDetail newCartDetail = CartDetailMapper.toEntity(mapProductId.get(productid));
                newCartDetail.setProducts(product);
                newCartDetail.setCarts(cart.get());
                cartDetailService.save(newCartDetail);
            }
        } else {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }


    private Map<String, CartDetailDto> getMapCartDetailDto(List<CartDetailDto> cartDetailDtos) {
        Map<String, CartDetailDto> result = new HashMap<>();
        for (CartDetailDto cartDetailDto : cartDetailDtos) {
            result.put(cartDetailDto.getProductId(), cartDetailDto);
        }
        return result;
    }
}

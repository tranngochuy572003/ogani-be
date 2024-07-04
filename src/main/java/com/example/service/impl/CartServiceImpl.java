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
            }
            List<CartDetail> cartDetailList = cartDetailService.findByCarts(cart.get());
            List<String> listProductIdEntity = getListProductIdFromCartDetailList(cartDetailList);
            List<String> listProductIdDto = getListProductIdFromCartDetailDtoList(cartDetailDtoList);

            for (String productId : listProductIdEntity) {
               if(!listProductIdDto.contains(productId)){
                   cartDetailService.deleteCartDetailByProductId(productId);
               }
            }
            for (CartDetailDto cartDetailDto : cartDetailDtoList) {
                Product product = productService.findProductById(cartDetailDto.getProductId());
                if (listProductIdEntity.contains(cartDetailDto.getProductId())) {
                    CartDetail cartDetail = cartDetailService.findByProducts(product);
                    CartDetailMapper.toUpdateEntity(cartDetail, cartDetailDto);
                    cartDetailService.save(cartDetail);
                } else {
                    CartDetail newCartDetail = CartDetailMapper.toEntity(cartDetailDto);
                    newCartDetail.setProducts(product);
                    newCartDetail.setCarts(cart.get());
                    cartDetailService.save(newCartDetail);
                }
            }
        } else {
            throw new BadRequestException(VALUE_EXISTED);
        }
    }

    @Override
    public List<String> getListProductIdFromCartDetailList(List<CartDetail> cartDetails) {
        List<String> listProductIdEntity = new ArrayList<>();
        for (CartDetail cartDetail : cartDetails) {
            listProductIdEntity.add(cartDetail.getProducts().getId());
        }
        return listProductIdEntity;
    }

    @Override
    public List<String> getListProductIdFromCartDetailDtoList(List<CartDetailDto> cartDetailDtos) {
        List<String> listProductIdDto = new ArrayList<>();
        for (CartDetailDto cartDetailDto : cartDetailDtos) {
            listProductIdDto.add(cartDetailDto.getProductId());
        }
        return listProductIdDto;
    }
}

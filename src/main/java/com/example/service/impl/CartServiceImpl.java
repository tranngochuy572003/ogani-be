package com.example.service.impl;

import com.example.dto.CartDetailDto;
import com.example.dto.CartDetailInfoDto;
import com.example.dto.CartDto;

import com.example.entity.*;
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

    private void checkProductValid(Product product) {
        if (product == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        if (!product.isActive()) {
            throw new BadRequestException(ITEM_UNACTIVED);
        }
        if (product.getInventory() == 0) {
            throw new BadRequestException(OUT_OF_STOCK);
        }
    }

    private void checkCartDetailValid(CartDetailDto cartDetailDto) {
        Product product = productService.findProductById(cartDetailDto.getProductId());
        checkProductValid(product);
        if (cartDetailDto.getQuantityProduct() <= 0) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (product.getInventory() < cartDetailDto.getQuantityProduct()) {
            throw new BadRequestException(PRODUCT_QUANTITY_UNENOUGH);
        }
    }

    @Override
    public void createCart(String userId, List<CartDetailDto> cartDetailDtos) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUsers(user);
        if (user == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        if (cart != null) {
            throw new BadRequestException(VALUE_EXISTED);
        }
        Cart newCart = new Cart();
        List<CartDetail> cartDetailList = new ArrayList<>();
        for (CartDetailDto cartDetailDto : cartDetailDtos) {
            checkCartDetailValid(cartDetailDto);
        }

        for (CartDetailDto cartDetailInList : cartDetailDtos) {
            Product product = productService.findProductById(cartDetailInList.getProductId());
            CartDetail cartDetail = CartDetailMapper.toEntity(cartDetailInList);
            newCart.setUsers(user);
            cartRepository.save(newCart);
            cartDetail.setProducts(product);
            cartDetail.setCarts(newCart);
            cartDetailList.add(cartDetail);
            newCart.setCartDetails(cartDetailList);
            cartRepository.save(newCart);
        }

    }

    @Override
    public void updateCart(String cartId, List<CartDetailDto> cartDetailDtoList) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isPresent()) {
            if (cartDetailDtoList.isEmpty()) {
                cartDetailService.deleteAllByCarts(cart.get());
                deleteCartById(cartId);
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
            for (Map.Entry<String, CartDetailDto> entry : mapProductId.entrySet()) {
                Product product = productService.findProductById(entry.getKey());
                CartDetail newCartDetail = CartDetailMapper.toEntity(entry.getValue());
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
            checkCartDetailValid(cartDetailDto);
            result.put(cartDetailDto.getProductId(), cartDetailDto);
        }
        return result;
    }

    @Override
    public CartDto getByUserId(String userId) {
        long totalPrice = 0L;
        List<CartDetailInfoDto> cartDetailInfoDto = new ArrayList<>();
        Cart cart = cartRepository.findByUserId(userId);
        if(cart==null){
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        List<CartDetail> cartDetailList = cartDetailService.findByCarts(cart);
        for (CartDetail cartDetail : cartDetailList) {
            Product product = productService.findProductById(cartDetail.getProducts().getId());
            List<String> imageUrls = new ArrayList<>();
            if (product.getImages() != null) {
                for (Image image : product.getImages()) {
                    imageUrls.add(image.getUrlImg());
                }
            }
            CartDetailInfoDto cartDetailInfo = new CartDetailInfoDto(product.getId(), product.getNameProduct(), imageUrls, cartDetail.getQuantityProduct(), product.getPrice(), cartDetail.isChosen());
            if (cartDetail.isChosen()) {
                totalPrice += product.getPrice() * cartDetail.getQuantityProduct();
            }
            cartDetailInfoDto.add(cartDetailInfo);
        }
        return new CartDto(cart.getId(), cart.getUsers().getId(), totalPrice, cartDetailInfoDto);
    }

    @Override
    public CartDto getByCartId(String cartId) {
        Optional<Cart> cart = cartRepository.findById(cartId);
        if (cart.isPresent()) {
            String userId = cart.get().getUsers().getId();
            return getByUserId(userId);
        } else {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
    }

    @Override
    public void deleteCartById(String cartId) {
        cartRepository.deleteCartById(cartId);
    }

    @Override
    public Cart getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if (cart == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        } else {
            return cart;
        }
    }

    @Override
    public void save(Cart cart) {
        cartRepository.save(cart);
    }


}

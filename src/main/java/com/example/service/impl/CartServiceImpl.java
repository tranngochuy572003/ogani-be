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


    @Override
    public void createCart(String userId, List<CartDetailDto> cartDetailDto) {
        User user = userService.findUserById(userId);
        Cart cart = cartRepository.findByUsers(user);
        if (user == null) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        if (cart == null) {
            Cart newCart = new Cart();
            List<CartDetail> cartDetailList = new ArrayList<>();
            for (CartDetailDto cartDetailInList : cartDetailDto) {
                Product product = productService.findProductById(cartDetailInList.getProductId());
                if (cartDetailInList.getQuantityProduct() <= 0) {
                    throw new BadRequestException(FIELD_INVALID);
                }
                if (product == null) {
                    throw new BadRequestException(VALUE_NO_EXIST);
                }
                if (!product.isActive()) {
                    throw new BadRequestException(ITEM_UNACTIVED);
                }
                if (product.getInventory() == 0) {
                    throw new BadRequestException(OUT_OF_STOCK);
                }
                if (product.getInventory() < cartDetailInList.getQuantityProduct()) {
                    throw new BadRequestException(PRODUCT_QUANTITY_UNENOUGH);
                }
            }


            for (CartDetailDto cartDetailInList : cartDetailDto) {
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
        } else {
            throw new BadRequestException(VALUE_EXISTED);
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
            for (String productId : mapProductId.keySet()) {
                Product product = productService.findProductById(productId);
                CartDetail newCartDetail = CartDetailMapper.toEntity(mapProductId.get(productId));
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
            Product product = productService.findProductById(cartDetailDto.getProductId());
            if (product == null) {
                throw new BadRequestException(VALUE_NO_EXIST);
            }
            if (!product.isActive()) {
                throw new BadRequestException(ITEM_UNACTIVED);
            }
            if (product.getInventory() == 0) {
                throw new BadRequestException(OUT_OF_STOCK);
            }
            if (product.getInventory() < cartDetailDto.getQuantityProduct()) {
                throw new BadRequestException(PRODUCT_QUANTITY_UNENOUGH);
            }
            result.put(cartDetailDto.getProductId(), cartDetailDto);
        }
        return result;
    }

    @Override
    public CartDto getByUserId(String userId) {
        long totalPrice = 0L;
        List<CartDetailInfoDto> cartDetailInfoDto = new ArrayList<>();
        Cart cart = cartRepository.findByUserId(userId);
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

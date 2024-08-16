package com.example.ogani.service;

import com.example.dto.CartDetailDto;
import com.example.dto.CartDetailInfoDto;
import com.example.dto.CartDto;
import com.example.entity.*;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.repository.CartRepository;
import com.example.service.CartDetailService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.VALUE_NO_EXIST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CartServiceTest {
    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    @Mock
    CartDetailService cartDetailService;

    private CartDto cartDto;
    private List<CartDetailInfoDto> cartDetailInfoDtoList;

    private List<CartDetail> cartDetailList;
    private List<CartDetailDto> cartDetailDtoList;
    private User user;
    private Cart cart;
    private Product product;
    private CartDetail cartDetail;
    private CartDetailDto cartDetailDto;
    private CartDetailInfoDto cartDetailInfoDto;

    @Mock
    private CartRepository cartRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("userId");
        user.setRole(UserRole.ADMIN);
        cartDetailInfoDto = new CartDetailInfoDto("productId", "name", null, 10L, 100L, true);
        cartDetailInfoDtoList = new ArrayList<>();
        cartDetailInfoDtoList.add(cartDetailInfoDto);
        cartDto = new CartDto("cartId", "userId", 100L, cartDetailInfoDtoList);
        product = new Product("name", true, 100L, "description", "information", 100L, null, null, cartDetailList);
        product.setId("productId");
        cartDetail = new CartDetail(true, 10L, product, cart);
        cartDetailList = new ArrayList<>();
        cartDetailList.add(cartDetail);
        cart = new Cart(cartDetailList, user);
        cartDetailDto = new CartDetailDto(true, 10L, "productId");
        cartDetailDto.setProductId("productId");
        cartDetailDtoList = new ArrayList<>();
        cartDetailDtoList.add(cartDetailDto);
    }

    @Test
    void testCreateCartThenSuccess() {
        when(userService.findUserById("userId")).thenReturn(user);
        when(cartRepository.findByUsers(user)).thenReturn(null);
        when(productService.findProductById("productId")).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.createCart("userId", cartDetailDtoList);
        verify(userService).findUserById("userId");
        verify(cartRepository).findByUsers(user);
        verify(productService, times(2)).findProductById("productId");
        verify(cartRepository, times(2)).save(any(Cart.class));

    }


    @Test
    void testCreateCartExisted() {
        when(userService.findUserById(anyString())).thenReturn(user);
        when(cartRepository.findByUsers(user)).thenReturn(cart);
        assertThrows(BadRequestException.class, () -> cartService.createCart("userId", cartDetailDtoList));
    }

    @Test
    void testCreateCartUserIdInvalidThenThrowBadRequest() {
        when(userService.findUserById("IdInvalid")).thenReturn(null);
        assertThrows(BadRequestException.class, () -> {
            cartService.createCart("IdInvalid", cartDetailDtoList);
        });
        verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testUpdateCartWhenCartDetailNullThenDeleteCart() {
        when(cartRepository.findById("cartId")).thenReturn(Optional.of(cart));
        cartService.updateCart("cartId", Collections.emptyList());
        verify(cartDetailService).deleteAllByCarts(cart);
    }


    @Test
    void testUpdateCartThenSuccess() {
        when(cartRepository.findById("cartId")).thenReturn(Optional.of(cart));
        when(cartDetailService.findByCarts(cart)).thenReturn(cartDetailList);
        when(productService.findProductById("productId")).thenReturn(product);
        cartService.updateCart("cartId", cartDetailDtoList);

    }



    @Test
    void testGetByUserIdValidThenSuccess() {
        Image image = new Image();

        when(cartRepository.findByUserId("userId")).thenReturn(cart);
        when(cartDetailService.findByCarts(cart)).thenReturn(cartDetailList);
        when(productService.findProductById("productId")).thenReturn(product);
        product.setImages(List.of(image));

        cartService.getByUserId("userId");
        Assertions.assertEquals(cart.getUsers().getId(), cartDto.getUserId());
    }

    @Test
    void testGetByUserIdInvalidThenThrowBadRequest() {
        when(cartRepository.findByUserId("userIdInvalid")).thenReturn(null);
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> cartService.getByUserId("userId"));
        Assertions.assertEquals(VALUE_NO_EXIST, badRequestException.getMessage());
    }


    @Test
    void testGetByCartIdInValidThenThrowBadRequest() {
        when(cartRepository.findById("idInvalid")).thenReturn(null);
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> cartService.getByCartId(null));
        Assertions.assertEquals(VALUE_NO_EXIST, badRequestException.getMessage());
    }

    @Test
    void testGetByCartIdValidThenSuccess() {
        when(cartRepository.findById("cartId")).thenReturn(Optional.ofNullable(cart));
        when(cartRepository.findByUserId(cart.getUsers().getId())).thenReturn(cart);
        cartService.getByCartId("cartId");
        Assertions.assertEquals(cart.getUsers().getId(), cartDto.getUserId());
    }

    @Test
    void testDeleteCart() {
        cartService.deleteCartById("cartId");
        verify(cartRepository).deleteCartById("cartId");
    }

    @Test
    void testGetCartByUserIdValidThenSuccess() {
        when(cartRepository.findByUserId("userId")).thenReturn(cart);
        cartService.getCartByUserId("userId");
        Assertions.assertEquals("userId", cart.getUsers().getId());
    }

    @Test
    void testGetCartByUserIdInValidThenThrowBadRequest() {
        when(cartRepository.findByUserId("userId")).thenReturn(null);
        assertThrows(BadRequestException.class, () -> cartService.getCartByUserId("userId"));
    }
}

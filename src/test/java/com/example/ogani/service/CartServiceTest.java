package com.example.ogani.service;

import com.example.dto.CartDetailDto;
import com.example.dto.CartDetailInfoDto;
import com.example.dto.CartDto;
import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.repository.CartRepository;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.VALUE_NO_EXIST;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {
    @InjectMocks
    private CartServiceImpl cartService;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;

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
        user = new User("userId", LocalDateTime.now(), LocalDateTime.now(), "", "", "fullName1", "userName1", "password1", "address1", "phoneNumber1", UserRole.CUSTOMER, false, null, null, null, null);
        cartDetailInfoDto = new CartDetailInfoDto("productId", "name", null, 10L, 100L, true);
        cartDetailInfoDtoList = new ArrayList<>();
        cartDetailInfoDtoList.add(cartDetailInfoDto);
        cartDto = new CartDto("cartId", "userId", 100L, cartDetailInfoDtoList);
        product = new Product("name", true, 100L, "description", "information", 100L, null,null, cartDetailList);
        cartDetail = new CartDetail(true, 10L, product, cart);
        cartDetailList = new ArrayList<>();
        cartDetailList.add(cartDetail);
        cart = new Cart(cartDetailList, user);
        cartDetailDto = new CartDetailDto(true, 10L, "productId");
        cartDetailDtoList = new ArrayList<>();
        cartDetailDtoList.add(cartDetailDto);
    }

    @Test
    void testCreateCartThenSuccess()  {
        when(userService.findUserById("userId")).thenReturn(user);
        when(cartRepository.findByUsers(user)).thenReturn(null);
        when(productService.findProductById("productId")).thenReturn(product);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.createCart("userId", cartDetailDtoList);

        verify(userService, times(1)).findUserById("userId");
        verify(cartRepository, times(1)).findByUsers(user);
        verify(productService, times(1)).findProductById("productId");
        verify(cartRepository, times(2)).save(any(Cart.class));

    }

    @Test
    void testCreateCartUserIdInvalidThenThrowBadRequest()  {
        when(userService.findUserById("IdInvalid")).thenReturn(null);
        assertThrows(BadRequestException.class, () -> {
            cartService.createCart("IdInvalid", cartDetailDtoList);
        });
                verify(cartRepository, never()).save(any(Cart.class));
    }

    @Test
    void testGetByUserIdValidThenSuccess() {
        Mockito.when(cartRepository.findByUserId("userId")).thenReturn(cart);
        cartService.getByUserId("userId");
        Assertions.assertEquals(cart.getUsers().getId(), cartDto.getUserId());
    }

    @Test
    void testGetByUserIdInvalidThenThrowBadRequest(){
        Mockito.when(cartRepository.findByUserId("userIdInvalid")).thenReturn(null);
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> cartService.getByUserId(any(String.class)));
        Assertions.assertEquals(VALUE_NO_EXIST, badRequestException.getMessage());
    }

    @Test
    void testGetByCartIdInValidThenThrowBadRequest() {
        Mockito.when(cartRepository.findById("idInvalid")).thenReturn(null);
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> cartService.getByCartId(any(String.class)));
        Assertions.assertEquals(VALUE_NO_EXIST, badRequestException.getMessage());
    }

    @Test
    void testGetByCartIdValidThenSuccess() {
        Mockito.when(cartRepository.findById("cartId")).thenReturn(Optional.ofNullable(cart));
        Mockito.when(cartRepository.findByUserId(cart.getUsers().getId())).thenReturn(cart);
        cartService.getByCartId("cartId");
        Assertions.assertEquals(cart.getUsers().getId(), cartDto.getUserId());
    }


}

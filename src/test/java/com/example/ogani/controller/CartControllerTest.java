package com.example.ogani.controller;


import com.example.controller.CartController;
import com.example.dto.CartDetailDto;
import com.example.dto.CartDetailInfoDto;
import com.example.dto.CartDto;
import com.example.entity.Cart;
import com.example.entity.CartDetail;
import com.example.entity.Product;
import com.example.entity.User;
import com.example.enums.UserRole;
import com.example.exception.BadRequestException;
import com.example.service.CartDetailService;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.common.MessageConstant.ITEM_CREATED_SUCCESS;
import static com.example.common.MessageConstant.VALUE_NO_EXIST;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CartControllerTest {
    @InjectMocks
    private CartController cartController;
    @Mock
    private CartService cartService;
    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    @Mock
    private CartDetailService cartDetailService;


    private CartDto cartDto;
    private List<CartDetailInfoDto> cartDetailInfoDtoList;

    private List<CartDetail> cartDetailList;
    private List<CartDetailDto> cartDetailDtoList;


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private User user;
    private Cart cart;
    private Product product;
    private CartDetail cartDetail;
    private CartDetailDto cartDetailDto;
    private CartDetailInfoDto cartDetailInfoDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
        objectMapper = new ObjectMapper();
        user = new User("userId", LocalDateTime.now(), LocalDateTime.now(), "", "", "fullName1", "userName1", "password1", "address1", "phoneNumber1", UserRole.CUSTOMER, false,  null, null, null, null);
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
        cartDetailDtoList = new ArrayList<>();
        cartDetailDtoList.add(cartDetailDto);

    }

    @Test
    void testCreateCartThenSuccess() throws Exception {
        cartService.createCart("userId", cartDetailDtoList);
        mockMvc.perform(post("/api/v1/carts/createCart/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartDetailDtoList)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ITEM_CREATED_SUCCESS));
    }
    @Test
    void testGetByUserIdValidThenSuccess() throws Exception {
        when(cartService.getByUserId("userId")).thenReturn(cartDto);
        mockMvc.perform(get("/api/v1/carts/getByUserId/{userId}", "userId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data.cartDetail[0].productId").value(cartDetailList.get(0).getProducts().getId()))
                .andExpect(jsonPath("$.data.cartDetail[0].name").value(cartDetailList.get(0).getProducts().getNameProduct()))
        ;
    }

    @Test
    void testGetByUserIdInValidThenThrowBadRequest() throws Exception {
        when(cartService.getByUserId("userIdInvalid")).thenThrow(new BadRequestException(VALUE_NO_EXIST));
        mockMvc.perform(get("/api/v1/carts/getByUserId/{userIdInvalid}", "userIdInvalid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof BadRequestException))
                .andExpect(result -> assertEquals(VALUE_NO_EXIST, result.getResolvedException().getMessage()));
    }

    @Test
    void testGetByCartIdThenSuccess() throws Exception {
        Mockito.when(cartService.getByCartId("cartId")).thenReturn(cartDto);
        mockMvc.perform(get("/api/v1/carts/getByCartId/{cartId}", "cartId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data.cartDetail[0].productId").value(cartDetailList.get(0).getProducts().getId()))
                .andExpect(jsonPath("$.data.cartDetail[0].name").value(cartDetailList.get(0).getProducts().getNameProduct()))
        ;
    }

}

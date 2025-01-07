package com.example.ogani.service;

import com.example.dto.*;
import com.example.entity.*;
import com.example.exception.BadRequestException;
import com.example.mapper.BillDetailMapper;
import com.example.repository.BillRepository;
import com.example.service.BillDetailService;
import com.example.service.CartService;
import com.example.service.ProductService;
import com.example.service.UserService;
import com.example.service.impl.BillServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class BillServiceTest {
    @InjectMocks
    private BillServiceImpl billService;
    @Mock
    private BillRepository billRepository;
    private Bill bill;

    @Mock
    private UserService userService;
    @Mock
    private ProductService productService;
    private Category category;
    @Mock
    private CartService cartService;

    private User user;

    private BillDto billDto;

    private CartDto cartDto;
    private CartDetailInfoDto cartDetailInfoDto;
    private ProductDto productDto;
    private CartDetail cartDetail;
    @Mock
    private BillDetailService billDetailService;

    private Cart cart;
    private Product product;
    private Image image;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId("userId");
        bill = new Bill();
        bill.setUsers(user);
        bill.setId("id");

        BillDetail billDetail1 = new BillDetail("urlImg", "nameProduct1", 100L, 2L, bill);
        BillDetail billDetail2 = new BillDetail("urlImg", "nameProduct2", 100L, 2L, bill);

        bill.setBillDetailList(List.of(billDetail1, billDetail2));
        billDto = new BillDto(bill.getId(), bill.getUsers().getId(), bill.getCreatedDate(), 400L, false, BillDetailMapper.toListDto(List.of(billDetail1, billDetail2)));

        category = new Category("name", "type", true, Collections.emptyList());
        image = new Image("url1", product);
        cartDetail = new CartDetail(true, 10L, product, cart);
        product = new Product("nameProduct1", true, 100L, "description", "information", 100L, List.of(image), category, List.of(cartDetail));
        product.setId("productId");

        cartDetailInfoDto = new CartDetailInfoDto("productId", "nameProduct1", List.of(image.getUrlImg()), 10L, 100L, true);

        cartDto = new CartDto("cartId", "userId", 100L, List.of(cartDetailInfoDto));
        productDto = new ProductDto("nameProduct1", true, "100L", "description", "information", "100L", category.getName(), List.of(image.getUrlImg()));

        cart = new Cart(List.of(cartDetail), user);

    }

    @Test
    void testGetBillByIdThenSuccess() {
        when(billRepository.findById(anyString())).thenReturn(Optional.of(bill));
        BillDto billDtoActual = billService.getBillById("id");
        Assertions.assertEquals(billDto, billDtoActual);
    }

    @Test
    void testGetBillByIdInvalidThenThrowBadRequest() {
        when(billRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () -> billService.getBillById("id"));
    }

    @Test
    void testGetBillByUserIdValidThenSuccess() {
        when(userService.findUserById(anyString())).thenReturn(user);
        when(billRepository.findByUsers(user)).thenReturn(List.of(bill));
        when(billRepository.findById(anyString())).thenReturn(Optional.of(bill));
        BillDto billDtoActual = billService.getBillById("id");
        List<BillDto> listBillDtoActual = billService.getBillByUserId(anyString());
        Assertions.assertEquals(listBillDtoActual, List.of(billDtoActual));
    }

    @Test
    void testConfirmOrderThenSuccess() {

        Product product1 = new Product("nameProduct1", true, 100L, "description", "information", 100L, null, category, null);
        Product product2 = new Product("nameProduct2", true, 100L, "description", "information", 100L, null, category, null);
        when(billRepository.findById(anyString())).thenReturn(Optional.of(bill));
        when(productService.findProductByName("nameProduct1")).thenReturn(product1);
        when(productService.findProductByName("nameProduct2")).thenReturn(product2);
        billService.confirmOrder(anyString());
        verify(billRepository).save(any(Bill.class));

    }

    @Test
    void testConfirmOrderWhenBillIdInvalidThenThrowBadRequest() {
        when(billRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () -> billService.confirmOrder("id"));
    }

    @Test
    void testConfirmOrderWhenBillIsConfirmedThenThrowBadRequest() {
        when(billRepository.findById(anyString())).thenReturn(Optional.of(bill));
        bill.setConfirm(true);
        Assertions.assertThrows(BadRequestException.class, () -> billService.confirmOrder("id"));
    }

    @Test
    void testIsAuthorizedToGetBillWhenBillIdInvalidThenThrowBadRequest() {
        when(billRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () -> billService.isAuthorizedToGetBill("id", "userId"));
    }

    @Test
    void testIsAuthorizedToGetBillThenSuccess() {
        when(billRepository.findById(anyString())).thenReturn(Optional.of(bill));
        Assertions.assertTrue(billService.isAuthorizedToGetBill("id", "userId"));
    }

    @Captor
    ArgumentCaptor<Bill> billCaptor;

    @Test
    void testOrderThenSuccess() {
        when(userService.findUserById("userId")).thenReturn(user);
        when(cartService.getByUserId("userId")).thenReturn(cartDto);
        when(productService.getProductById(product.getId())).thenReturn(productDto);
        when(cartService.getCartByUserId(bill.getUsers().getId())).thenReturn(cart);
        billService.order("userId");

        verify(billRepository,times(2)).save(billCaptor.capture());
        Bill value = billCaptor.getValue();
        assertEquals("....","userId" ,value.getUsers().getId());
    }

}

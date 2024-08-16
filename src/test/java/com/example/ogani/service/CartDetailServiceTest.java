package com.example.ogani.service;

import com.example.entity.*;
import com.example.repository.CartDetailRepository;
import com.example.service.impl.CartDetailServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

class CartDetailServiceTest {
    @InjectMocks
    CartDetailServiceImpl cartDetailServiceImpl ;
    @Mock
    CartDetailRepository cartDetailRepository;
    private CartDetail cartDetail;
    private User user;
    private Cart cart;
    private Product product;
    private Category category;
    private Image image;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        image = new Image("url1", product);
        category = new Category("name", "type", true, Collections.emptyList());
        cartDetail = new CartDetail(true, 10L, product, cart);

        product = new Product("nameProduct1", true, 100L, "description", "information", 100L, List.of(image), category, List.of(cartDetail));
        user = new User();
        user.setId("userId");
        cart = new Cart(List.of(cartDetail), user);

    }

    @Test
    void testDeleteAllByCarts(){
        cartDetailServiceImpl.deleteAllByCarts(cart);
        verify(cartDetailRepository).deleteAllByCarts(cart);

    }

    @Test
    void testFindByCarts(){
        cartDetailServiceImpl.findByCarts(cart);
        verify(cartDetailRepository).findByCarts(cart);
    }
    @Test
    void testSaveCartDetail(){
        cartDetailServiceImpl.save(cartDetail);
        verify(cartDetailRepository).save(cartDetail);
    }


    @Test
    void testDeleteCartDetailByProductId(){
        cartDetailServiceImpl.deleteCartDetailByProductId(anyString());
        verify(cartDetailRepository).deleteCartDetailByProducts(anyString());

    }


}

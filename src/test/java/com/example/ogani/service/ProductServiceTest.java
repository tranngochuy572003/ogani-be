package com.example.ogani.service;

import com.example.entity.Category;
import com.example.entity.Product;
import com.example.exception.NotFoundException;
import com.example.repository.ProductRepository;
import com.example.service.impl.ProductServiceImpl;
import com.example.util.AppUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static com.example.common.MessageConstant.VALUE_NO_EXIST;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    private Category category;

    private Product product;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product("name", true, 100L, "description", "information", 100L, null, null,null);
        category = new Category("name", "type", true, Arrays.asList(product));
        product.setCategory(category);
    }


    @Test
    void testGetProductByCreatedDate() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            LocalDate createdDate = LocalDate.now();
            appUtilMock.when(() -> AppUtil.checkDateValid(anyString())).thenReturn(createdDate);
            when(productRepository.findByCreatedDateBetween(any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(Arrays.asList(product));
            productService.getProductsByCreatedDate(createdDate);

            verify(productRepository).findByCreatedDateBetween(any(LocalDateTime.class),any(LocalDateTime.class));
        }
    }


    @Test
    void testDeleteByIdThenSuccess() {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        productService.deleteById(anyString());
        verify(productRepository).deleteById(anyString());
    }
    @Test
    void testDeleteByIdInvalidThenThrowNotFoundException() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> productService.deleteById(null));
        Assertions.assertEquals(VALUE_NO_EXIST, notFoundException.getMessage());
        verify(productRepository, never()).deleteById(anyString());
    }
}

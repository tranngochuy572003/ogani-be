package com.example.ogani.service;

import com.example.dto.ProductDto;
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


public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;
    @Mock
    private ProductRepository productRepository;
    private Category category;

    private Product product;
    private ProductDto productDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product("name", true, 100L, "description", "information", 100L, null, null,null);
        productDto = new ProductDto("nameProduct", true, 100L, "description", "information", 100L, "category", Arrays.asList("urlImg"));
        category = new Category("name", "type", true, Arrays.asList(product));
        product.setCategory(category);
    }


    @Test
    void testGetProductByCreatedDate() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            LocalDate createdDate = LocalDate.now();
            appUtilMock.when(() -> AppUtil.checkDateValid(anyString())).thenReturn(createdDate);
            Mockito.when(productRepository.findByCreatedDateBetween(any(LocalDateTime.class),any(LocalDateTime.class))).thenReturn(Arrays.asList(product));
            productService.getProductsByCreatedDate(createdDate);

            verify(productRepository, times(1)).findByCreatedDateBetween(any(LocalDateTime.class),any(LocalDateTime.class));
        }
    }


    @Test
    void testDeleteByIdThenSuccess() {
        Mockito.when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        productService.deleteById(anyString());
        verify(productRepository, times(1)).deleteById(anyString());
    }
    @Test
    void testDeleteByIdInvalidThenThrowNotFoundException() {
        Mockito.when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> productService.deleteById(anyString()));
        Assertions.assertEquals(VALUE_NO_EXIST, notFoundException.getMessage());
        verify(productRepository, never()).deleteById(anyString());
    }
}

package com.example.ogani.service;

import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.entity.Image;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.exception.BaseException;
import com.example.exception.NotFoundException;
import com.example.mapper.ProductMapper;
import com.example.repository.ProductRepository;
import com.example.service.CategoryService;
import com.example.service.FileUploadService;
import com.example.service.ImageService;
import com.example.service.impl.ProductServiceImpl;
import com.example.util.AppUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
    private ProductDto productDto;
    private MultipartFile[] multipartFiles;
    @Mock
    private CategoryService categoryService;
    @Mock
    private FileUploadService fileUpload;
    @Mock
    private ImageService imageService;

    private Image image;

    static Stream<Arguments> invalidProductInputs() {
        return Stream.of(
                Arguments.of("category//", "categoryInvalid"),
                Arguments.of("name//", "nameInvalid"),
                Arguments.of("inventory123", "inventoryInvalidFormat"),
                Arguments.of("price123", "priceInvalidFormat"),
                Arguments.of("-100", "priceLessThanZero"),
                Arguments.of("0", "priceEqualZero"),
                Arguments.of("-100", "inventoryLessThanZero"),
                Arguments.of("false", "categoryInActive"),
                Arguments.of("null", "categoryNull")

        );
    }


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        category = new Category("name", "type", true, Arrays.asList(product));
        product = new Product("name", true, 100L, "description", "information", 100L, null, category, null);
        productDto = new ProductDto("name", true, "100", "description", "information", "100", category.getName(), Collections.emptyList());

        multipartFiles = new MultipartFile[]{
                new MockMultipartFile("file1", "file1.txt", "text/plain", "some content".getBytes()),
                new MockMultipartFile("file2", "file2.txt", "text/plain", "some other content".getBytes())
        };
        image = new Image("url1", product);
        product.setImages(List.of(image));
        productDto.setImageList(List.of(image.getUrlImg()));

    }


    @Test
    void testGetProductByCreatedDate() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            LocalDate createdDate = LocalDate.now();
            appUtilMock.when(() -> AppUtil.checkDateValid(anyString())).thenReturn(createdDate);
            when(productRepository.findByCreatedDateBetween(any(LocalDateTime.class), any(LocalDateTime.class))).thenReturn(Arrays.asList(product));
            productService.getProductsByCreatedDate(createdDate);

            verify(productRepository).findByCreatedDateBetween(any(LocalDateTime.class), any(LocalDateTime.class));
        }
    }

    @Test
    void testGetProductByPriceValidThenSuccess() {
        Long priceLowest = 10L;
        when(productRepository.findByPriceBetween(priceLowest, product.getPrice())).thenReturn(List.of(product));
        productService.getProductByPrice(product.getPrice().toString());
        Assertions.assertEquals(List.of(productDto), ProductMapper.toListDto(List.of(product)));
    }

    @Test
    void testGetProductByPriceInValidThenThrowBadRequestException() {
        Assertions.assertThrows(BaseException.class, () -> productService.getProductByPrice("price"));
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


    @Test
    void testAddProductThenSuccess() throws IOException {
        when(categoryService.findCategoryByName(productDto.getCategory())).thenReturn(category);
        when(fileUpload.uploadFiles(multipartFiles, product)).thenReturn(List.of(image));
        productService.addProduct(productDto, multipartFiles);
        Product productActual = ProductMapper.toCreateEntity(productDto);
        productActual.setCategory(category);
        productActual.setImages(List.of(image));
        Assertions.assertEquals(product, productActual);

    }

    @ParameterizedTest
    @MethodSource("invalidProductInputs")
    void testAddProductWhenInputInvalidThenThrowBadRequest(String input, String field) {
        when(categoryService.findCategoryByName(productDto.getCategory())).thenReturn(category);
        switch (field) {
            case "categoryInvalid", "categoryNull" -> productDto.setCategory(input);
            case "nameInvalid" -> productDto.setNameProduct(input);
            case "priceInvalidFormat", "priceLessThanZero", "priceEqualZero" -> productDto.setPrice(input);
            case "inventoryInvalidFormat", "inventoryLessThanZero" -> productDto.setInventory(input);
            case "categoryInActive" -> category.setActive(Boolean.parseBoolean(input));

            default -> throw new IllegalArgumentException("Unexpected field: " + field);
        }

        Assertions.assertThrows(BadRequestException.class, () -> productService.addProduct(productDto, multipartFiles));

    }

    @Test
    void testAddProductWhenProductNameExistedThenThrowBadRequest() {
        when(categoryService.findCategoryByName(productDto.getCategory())).thenReturn(category);
        when(productRepository.findProductByNameProduct(anyString())).thenReturn(product);
        product.setCategory(category);
        Assertions.assertThrows(BadRequestException.class, () -> productService.addProduct(productDto, multipartFiles));
    }

    @Captor
    ArgumentCaptor<Product> productDtoArgumentCaptor;


    @Test
    void testUpdateProductThenSuccess() {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(categoryService.findCategoryByName(anyString())).thenReturn(category);
        when(productRepository.findProductByNameProduct(anyString())).thenReturn(null);
        productService.updateProduct(anyString(), productDto, multipartFiles);
        verify(productRepository).save(productDtoArgumentCaptor.capture());
        Assertions.assertEquals(productDtoArgumentCaptor.getValue().getCategory(), category);
    }

    @Test
    void testUpdateProductWhenIdInvalidThenThrowBadRequest() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () ->  productService.updateProduct("id", productDto, multipartFiles));

    }
    @ParameterizedTest
    @MethodSource("invalidProductInputs")
    void testUpdateProductWhenInputInvalidThenThrowBadRequest(String input, String field) {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        when(categoryService.findCategoryByName(productDto.getCategory())).thenReturn(category);
        switch (field) {
            case "categoryInvalid", "categoryNull" -> productDto.setCategory(input);
            case "nameInvalid" -> productDto.setNameProduct(input);
            case "priceInvalidFormat", "priceLessThanZero", "priceEqualZero" -> productDto.setPrice(input);
            case "inventoryInvalidFormat", "inventoryLessThanZero" -> productDto.setInventory(input);
            case "categoryInActive" -> category.setActive(Boolean.parseBoolean(input));

            default -> throw new IllegalArgumentException("Unexpected field: " + field);
        }

        Assertions.assertThrows(BadRequestException.class, () ->productService.updateProduct("id", productDto, multipartFiles));

    }
    @Test
    void testGetAllProductThenSuccess() {
        List<Product> productList = List.of(product);
        when(productRepository.findAll()).thenReturn(productList);
        List<ProductDto> productListActual = productService.getAllProducts();
        Assertions.assertEquals(ProductMapper.toListDto(productList), productListActual);
    }

    @Test
    void testGetProductByIdThenSuccess() {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        productService.getProductById(anyString());
        Assertions.assertEquals(ProductMapper.toDto(product), productDto);

    }

    @Test
    void testGetProductByIdInvalidThenThrowBadRequest() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> productService.getProductById("productIdInvalid"));
    }

    @Test
    void testFindProductByIdThenSuccess() {
        when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
        Product productActual = productService.findProductById(anyString());
        Assertions.assertEquals(product, productActual);

    }

    @Test
    void testFindProductByIdInvalidThenThrowBadRequest() {
        when(productRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> productService.findProductById("productIdInvalid"));
    }

    @Test
    void testFindProductByNameThenSuccess() {
        when(productRepository.findProductByNameProduct(anyString())).thenReturn(product);
        Product productActual = productService.findProductByName(anyString());
        Assertions.assertEquals(product, productActual);

    }

    @Test
    void testFindProductByNameInvalidThenSuccess() {
        when(productRepository.findProductByNameProduct(anyString())).thenReturn(null);
        Assertions.assertThrows(BadRequestException.class, () -> productService.findProductByName("nameProductInvalid"));
    }

    @Test
    void testGetProductByNameThenSuccess() {
        when(productRepository.findProductByNameProduct(anyString())).thenReturn(product);
        ProductDto productDtoActual = productService.getProductByName(anyString());
        Assertions.assertEquals(ProductMapper.toDto(product), productDtoActual);

    }

    @Test
    void testGetProductByNameInvalidThenThrowBadRequest() {
        when(productRepository.findProductByNameProduct(anyString())).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> productService.getProductByName("nameProductInvalid"));
    }


}


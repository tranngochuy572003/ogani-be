package com.example.ogani.controller;

import com.example.controller.ProductController;
import com.example.dto.ProductDto;
import com.example.entity.Category;
import com.example.service.impl.ProductServiceImpl;
import com.example.util.AppUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;

import static com.example.common.MessageConstant.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {
    @InjectMocks
    private ProductController productController;
    @Mock
    private ProductServiceImpl productService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Category category;
    private ProductDto productDto;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
        objectMapper = new ObjectMapper();
        productDto = new ProductDto("nameProduct", true, 100L, "description", "information", 100L, "category", Arrays.asList("urlImg"));
    }

    @Test
    void testAddProductThenSuccess() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "filename.jpg",
                "image/jpeg",
                "some image content".getBytes()
        );


        MockMultipartFile productDto = new MockMultipartFile(
                "productDto",
                "",
                "application/json",
                "{\"nameProduct\":\"nameProduct\",\"inventory\":100,\"description\":\"description\",\"information\":\"information\",\"price\":100,\"category\":\"category\",\"imageList\":[\"urlImg\"],\"isActive\":true}".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/products/add")
                        .file(productDto)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ITEM_CREATED_SUCCESS));
    }


    @Test
    void testGetAllProductsThenSuccess() throws Exception {
        mockMvc.perform(get("/api/v1/products/getAllProducts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Successfully"));

    }

    @Test
    void testUpdateProductThenSuccess() throws Exception {
        MockMultipartFile image = new MockMultipartFile(
                "image",
                "filename.jpg",
                "image/jpeg",
                "some image content".getBytes()
        );


        MockMultipartFile productDto = new MockMultipartFile(
                "productDto",
                "",
                "application/json",
                "{\"nameProduct\":\"nameProduct\",\"inventory\":100,\"description\":\"description\",\"information\":\"information\",\"price\":100,\"category\":\"category\",\"imageList\":[\"urlImg\"],\"isActive\":true}".getBytes()
        );

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/products/update/{id}", "id");
        builder.with(request -> {
            request.setMethod("PATCH");
            return request;
        });
        mockMvc.perform(builder
                        .file(productDto)
                        .file(image)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value(ITEM_UPDATED_SUCCESS));
    }


    @Test
    void testGetProductByIdThenSuccess() throws Exception {
        String productDtoJson = objectMapper.writeValueAsString(productDto);
        String expectedJson = "{ \"message\": \"Successfully\", \"data\": " + productDtoJson + " }";

        Mockito.when(productService.getProductById("id")).thenReturn(productDto);

        mockMvc.perform(get("/api/v1/products/getProductById/{id}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(expectedJson));
    }

    @Test
    void testGetProductByCreatedDateThenSuccess() throws Exception {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            LocalDate createdDate = LocalDate.now();
            appUtilMock.when(() -> AppUtil.checkDateValid(anyString())).thenReturn(createdDate);
            String productDtoJson = objectMapper.writeValueAsString(Arrays.asList(productDto));
            String expectedJson = "{ \"message\": \"Successfully\", \"data\": " + productDtoJson + " }";
            Mockito.when(productService.getProductsByCreatedDate(createdDate)).thenReturn(Arrays.asList(productDto));

            mockMvc.perform(get("/api/v1/products/getProductByCreatedDate/{createdDate}", createdDate)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print())
                    .andExpect(content().json(expectedJson));
        }
    }




        @Test
        void testDeleteByIdThenSuccess () throws Exception {
            mockMvc.perform(delete("/api/v1/products/deleteById/{id}", "id")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(ITEM_DELETED_SUCCESS));

        }


    }

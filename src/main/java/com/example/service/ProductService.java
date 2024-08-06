package com.example.service;

import com.example.dto.ProductDto;
import com.example.entity.Product;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ProductService {
    void addProduct(ProductDto productDto, MultipartFile[] multipartFile) throws IOException;

    List<ProductDto> getAllProducts();

    void updateProduct(String id, ProductDto productDto, MultipartFile[] multipartFile) throws IOException;

    ProductDto getProductById(String id);

    Product findProductById(String id);

    Product findProductByName(String name);

    ProductDto getProductByName(String name);

    List<ProductDto> getProductsByCreatedDate(LocalDate localDate);

    void deleteById(String id);

    List<ProductDto> getProductByPrice(String price);

    void save(Product product);

}

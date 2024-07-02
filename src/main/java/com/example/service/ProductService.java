package com.example.service;

import com.example.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {
  void addProduct(ProductDto productDto, MultipartFile [] multipartFile) throws IOException;

  List<ProductDto> getAllProducts();
  void updateProduct(String id , ProductDto productDto,MultipartFile [] multipartFile) throws IOException;

  ProductDto getProductById(String id);

}

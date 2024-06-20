package com.example.service;


import com.example.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {
  void addProduct(ProductDto productDto, MultipartFile [] multipartFile) throws IOException;

}

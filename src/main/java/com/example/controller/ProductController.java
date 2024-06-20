package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.common.MessageConstant.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController{
  @Autowired
  private ProductService productService;
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody ProductDto productDto){
    productService.addProduct(productDto);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_CREATED_SUCCESS);
    return ResponseEntity.ok(response);
  }
}

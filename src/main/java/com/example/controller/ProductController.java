package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static com.example.common.MessageConstant.ITEM_CREATED_SUCCESS;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController{
  @Autowired
  private ProductService productService;


  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(
          @RequestPart("productDto") ProductDto productDto,
          @RequestParam("image") MultipartFile multipartFile,
          Model model
  ) throws IOException {
    productService.addProduct(productDto, multipartFile,model);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_CREATED_SUCCESS);
    return ResponseEntity.ok(response);
  }

}

package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.example.common.MessageConstant.ITEM_CREATED_SUCCESS;
import static com.example.common.MessageConstant.ITEM_UPDATED_SUCCESS;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
  @Autowired
  private ProductService productService;


  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(
          @RequestPart("productDto") ProductDto productDto,
          @RequestParam("image") MultipartFile [] multipartFile) throws IOException {
      productService.addProduct(productDto, multipartFile);
      ApiResponse response = new ApiResponse(HttpStatus.OK.value());
      response.setMessage(ITEM_CREATED_SUCCESS);
      return ResponseEntity.ok(response);
  }

  @GetMapping("/getAllProducts")
  public ResponseEntity<ApiResponse> getAllProducts() {
    List<ProductDto> products = productService.getAllProducts();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),products));
  }

  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable String id, @RequestBody ProductDto productDto) {
    productService.updateProduct(id, productDto);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_UPDATED_SUCCESS);
    return ResponseEntity.ok(response);
  }

}

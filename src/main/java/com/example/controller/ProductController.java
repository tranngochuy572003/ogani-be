package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static com.example.common.MessageConstant.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
  @Autowired
  private ProductService productService;

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
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

  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable String id, @RequestPart("productDto") ProductDto productDto, @RequestParam(value = "image",required = false) MultipartFile [] multipartFile) throws IOException {
    productService.updateProduct(id, productDto,multipartFile);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_UPDATED_SUCCESS);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/getProductById/{id}")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable String id) {
    ProductDto productDto = productService.getProductById(id);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDto));
  }

  @GetMapping("/getProductByName/{name}")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
    ProductDto productDto = productService.getProductByName(name);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDto));

  }

  @GetMapping("/getProductByCreatedDate/{createdDate}")
  public ResponseEntity<ApiResponse> getProductByCreatedDate(@PathVariable String createdDate){
    LocalDate localDate = AppUtil.checkDateValid(createdDate);
    List<ProductDto> productDtoList= productService.getProductsByCreatedDate(localDate);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDtoList));

  }
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/deleteById/{id}")
  public ResponseEntity<ApiResponse> deleteById(@PathVariable String id){
    productService.deleteById(id);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_DELETED_SUCCESS);
    return ResponseEntity.ok(response);

  }


}

package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.ProductDto;
import com.example.service.ProductService;
import com.example.util.AppUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Product Controller")
public class ProductController {
  @Autowired
  private ProductService productService;

  @Operation(summary = "Add new product")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(
          @RequestPart("productDto") ProductDto productDto,
          @RequestParam(value = "image",required = false) MultipartFile [] multipartFile) throws IOException {
      productService.addProduct(productDto, multipartFile);
      ApiResponse response = new ApiResponse(HttpStatus.OK.value());
      response.setMessage(ITEM_CREATED_SUCCESS);
      return ResponseEntity.ok(response);
  }

  @Operation(summary = "Update product by id")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse> updateProduct(@PathVariable String id,
                                                   @RequestPart("productDto") ProductDto productDto,
                                                   @RequestParam(value = "image",required = false) MultipartFile [] multipartFile) throws IOException {
    productService.updateProduct(id, productDto,multipartFile);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_UPDATED_SUCCESS);
    return ResponseEntity.ok(response);
  }


  @Operation(summary = "Delete product by id")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping("/deleteById/{id}")
  public ResponseEntity<ApiResponse> deleteById(@PathVariable String id){
    productService.deleteById(id);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_DELETED_SUCCESS);
    return ResponseEntity.ok(response);

  }

  @Operation(summary = "Get all products")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @GetMapping("/getAllProducts")
  public ResponseEntity<ApiResponse> getAllProducts() {
    List<ProductDto> products = productService.getAllProducts();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),products));
  }

  @Operation(summary = "Get product by id")
  @GetMapping("/getProductById/{id}")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable String id) {
    ProductDto productDto = productService.getProductById(id);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDto));
  }

  @Operation(summary = "Get product by name")
  @GetMapping("/getProductByName/{name}")
  public ResponseEntity<ApiResponse> getProductByName(@PathVariable String name){
    ProductDto productDto = productService.getProductByName(name);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDto));

  }

  @Operation(summary = "Get list product by created date")
  @GetMapping("/getProductByCreatedDate/{createdDate}")
  public ResponseEntity<ApiResponse> getProductByCreatedDate(@PathVariable String createdDate){
    LocalDate localDate = AppUtil.checkDateValid(createdDate);
    List<ProductDto> productDtoList= productService.getProductsByCreatedDate(localDate);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDtoList));

  }

  @Operation(summary = "Get list product by price")
  @GetMapping("/getProductByPrice/{price}")
  public ResponseEntity<ApiResponse> getProductByPrice(@PathVariable String price){
    List<ProductDto> productDtoList = productService.getProductByPrice(price);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),productDtoList));
  }

}

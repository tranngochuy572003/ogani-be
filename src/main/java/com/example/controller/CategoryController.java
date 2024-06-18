package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.CategoryDto;
import com.example.service.CategoryService;
import com.example.util.AppUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import static com.example.common.MessageConstant.*;
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;
  @GetMapping("/getCategoriesByType/{type}")
  public ResponseEntity<ApiResponse> getCategoriesByType(@PathVariable String type) {
    List<CategoryDto> categories = categoryService.findByType(type);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @GetMapping("/getAllCategories")
  public ResponseEntity<ApiResponse> getAllCategories() {
    List<CategoryDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @GetMapping("/getById/{id}")
  public ResponseEntity<ApiResponse> getById(@PathVariable String id) {
    CategoryDto categoryDto = categoryService.findById(id);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categoryDto));
  }
  @GetMapping("/getByName/{name}")
  public ResponseEntity<ApiResponse> getByName(@PathVariable String name) {
    List<CategoryDto> categories = categoryService.findByName(name);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @GetMapping("/getCreatedDate/{createdDate}")
  public ResponseEntity<ApiResponse> getCreatedDate(@PathVariable String createdDate) throws ParseException {
    LocalDate localDate = AppUtil.checkDateValid(createdDate);
    List<CategoryDto> categories = categoryService.findByCreatedDate(localDate);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDto categoryDto) {
    categoryService.addCategory(categoryDto);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value()));
  }
  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
    categoryService.updateCategory(id, categoryDto);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value()));
  }
  @GetMapping("/getCategoriesActive")
  public ResponseEntity<ApiResponse> getCategoriesActive() {
    List<CategoryDto> categories = categoryService.getCategoriesActive();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
}

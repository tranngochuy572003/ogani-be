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

import static com.example.common.MessageConstant.ITEM_CREATED_SUCCESS;
import static com.example.common.MessageConstant.ITEM_UPDATED_SUCCESS;
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;
  @GetMapping("/getCategoriesByType/{type}")
  public ResponseEntity<ApiResponse<List<CategoryDto>>> getCategoriesByType(@PathVariable String type) {
    List<CategoryDto> categories = categoryService.findByType(type);
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),categories));
  }
  @GetMapping("/getAllCategories")
  public ResponseEntity<?> getAllCategories() {
    List<CategoryDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),categories));
  }
  @GetMapping("/getById/{id}")
  public ResponseEntity<?> getById(@PathVariable String id) {
    CategoryDto categoryDto = categoryService.findById(id);
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),categoryDto));
  }
  @GetMapping("/getByName/{name}")
  public ResponseEntity<?> getByName(@PathVariable String name) {
    List<CategoryDto> categories = categoryService.findByName(name);
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),categories));
  }
  @GetMapping("/getCreatedDate/{date}")
  public ResponseEntity<?> getCreatedDate(@PathVariable String date) throws ParseException {
    LocalDate localDate = AppUtil.checkDateValid(date);
    List<CategoryDto> categories = categoryService.findByCreatedDate(localDate);
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),categories));
  }
  @PostMapping("/add")
  public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {
    categoryService.addCategory(categoryDto);
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),ITEM_CREATED_SUCCESS));
  }
  @PatchMapping("/update/{id}")
  public ResponseEntity<?> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
    categoryService.updateCategory(id, categoryDto);
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),ITEM_UPDATED_SUCCESS));
  }
  @GetMapping("/getCategoriesActive")
  public ResponseEntity<?> getCategoriesActive() {
    List<CategoryDto> categories = categoryService.getCategoriesActive();
    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(),categories));
  }
}

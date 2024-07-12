package com.example.controller;

import com.example.api.ApiResponse;
import com.example.dto.CategoryDto;
import com.example.service.CategoryService;
import com.example.util.AppUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import static com.example.common.MessageConstant.*;
@RestController
@RequestMapping("/api/v1/categories")
@Tag(name="Category Controller")
public class CategoryController {
  @Autowired
  private CategoryService categoryService;
  @Operation(summary = "Get list category by type")
  @GetMapping("/getCategoriesByType/{type}")
  public ResponseEntity<ApiResponse> getCategoriesByType(@PathVariable String type) {
    List<CategoryDto> categories = categoryService.findByType(type);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @Operation(summary = "Get all categories")
  @GetMapping("/getAllCategories")
  public ResponseEntity<ApiResponse> getAllCategories() {
    List<CategoryDto> categories = categoryService.getAllCategories();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @Operation(summary = "Get category by id")
  @GetMapping("/getById/{id}")
  public ResponseEntity<ApiResponse> getById(@PathVariable String id) {
    CategoryDto categoryDto = categoryService.findById(id);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categoryDto));
  }
  @Operation(summary = "Get category by name")
  @GetMapping("/getByName/{name}")
  public ResponseEntity<ApiResponse> getByName(@PathVariable String name) {
    CategoryDto categoryDto = categoryService.findByName(name);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categoryDto));
  }
  @Operation(summary = "Get list category by createdDate")
  @GetMapping("/getCreatedDate/{createdDate}")
  public ResponseEntity<ApiResponse> getCreatedDate(@PathVariable String createdDate) throws ParseException {
    LocalDate localDate = AppUtil.checkDateValid(createdDate);
    List<CategoryDto> categories = categoryService.findByCreatedDate(localDate);
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }

  @Operation(summary = "Add new category")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addCategory(@RequestBody CategoryDto categoryDto) {
    categoryService.addCategory(categoryDto);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_CREATED_SUCCESS);
    return ResponseEntity.ok(response);
  }
  @Operation(summary = "Update category by id")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @PatchMapping("/update/{id}")
  public ResponseEntity<ApiResponse> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
    categoryService.updateCategory(id, categoryDto);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_UPDATED_SUCCESS);
    return ResponseEntity.ok(response);
  }
  @Operation(summary = "Get list category active")
  @GetMapping("/getCategoriesActive")
  public ResponseEntity<ApiResponse> getCategoriesActive() {
    List<CategoryDto> categories = categoryService.getCategoriesActive();
    return ResponseEntity.ok(new ApiResponse(HttpStatus.OK.value(),categories));
  }
  @Operation(summary = "Delete category by id")
  @PreAuthorize("hasAuthority('ROLE_ADMIN')")
  @DeleteMapping ("/deleteCategory/{id}")
  public ResponseEntity<ApiResponse> deleteCategory(@PathVariable String id) {
    categoryService.deleteCategory(id);
    ApiResponse response = new ApiResponse(HttpStatus.OK.value());
    response.setMessage(ITEM_DELETED_SUCCESS);
    return ResponseEntity.ok(response);
  }
}

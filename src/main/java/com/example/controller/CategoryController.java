package com.example.controller;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
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
  public ResponseEntity<?> getCategoriesByType(@PathVariable String type) {
    List<Category> categories = categoryService.findByType(type);
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @GetMapping("/getAllCategories")
  public ResponseEntity<?> getAllCategories() {
    List<Category> categories = categoryService.getAllCategories();
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @GetMapping("/getById/{id}")
  public ResponseEntity<?> getById(@PathVariable String id) {
    Category category = categoryService.findById(id);
    return new ResponseEntity<>(category, HttpStatus.OK);
  }

  @GetMapping("/getByName/{name}")
  public ResponseEntity<?> getByName(@PathVariable String name) {
    List<Category> categories = categoryService.findByName(name);
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @GetMapping("/getCreatedDate/{date}")
  public ResponseEntity<?> getCreatedDate(@PathVariable String date) throws ParseException {
    LocalDate localDate = AppUtil.checkDateValid(date);
    List<Category> categories = categoryService.findByCreatedDate(localDate);
    return new ResponseEntity<>(categories, HttpStatus.OK);
  }

  @PostMapping("/add")
  public ResponseEntity<String> addCategory(@RequestBody CategoryDto categoryDto) {
    categoryService.addCategory(categoryDto);
    return new ResponseEntity<>(ITEM_CREATED_SUCCESS, HttpStatus.OK);
  }

  @PatchMapping ("/update/{id}")
  public ResponseEntity<String> updateCategory(@PathVariable String id, @RequestBody CategoryDto categoryDto) {
    categoryService.updateCategory(id, categoryDto);
    return new ResponseEntity<>(ITEM_UPDATED_SUCCESS, HttpStatus.OK);
  }
}

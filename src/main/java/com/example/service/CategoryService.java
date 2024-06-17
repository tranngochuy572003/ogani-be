package com.example.service;

import com.example.dto.CategoryDto;
import com.example.dto.UserDto;
import com.example.entity.Category;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CategoryService {
  List<Category> getAllCategories();

  List<Category> findByType (String type) ;

  List<Category> findByName (String name) ;
  List<Category> findByCreatedDate (LocalDate date) throws ParseException;
  Category findById (String id) ;
  void addCategory(CategoryDto categoryDto);

  void updateCategory(String id , CategoryDto categoryDto) ;




}

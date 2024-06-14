package com.example.service;

import com.example.dto.CategoryDto;
import com.example.entity.Category;

import java.text.ParseException;
import java.util.List;

public interface CategoryService {
  List<Category> getAllCategories();

  List<Category> findByType (String type) ;

  List<Category> findByName (String name) ;
  List<Category> findByCreatedDate (String date) throws ParseException;

  Category findById (String id) ;
  void addCategory(CategoryDto categoryDto);



}

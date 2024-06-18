package com.example.service;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
public interface CategoryService {
  List<CategoryDto> getAllCategories();
  List<CategoryDto> findByType (String type) ;
  List<CategoryDto> findByName (String name) ;
  List<CategoryDto> findByCreatedDate (LocalDate date) throws ParseException;
  CategoryDto findById (String id) ;
  void addCategory(CategoryDto categoryDto);
  void updateCategory(String id , CategoryDto categoryDto) ;
  List<CategoryDto> getCategoriesActive() ;
  void deleteCategory(String id) ;

}

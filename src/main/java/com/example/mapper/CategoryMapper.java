package com.example.mapper;

import com.example.dto.CategoryDto;
import com.example.dto.CommentDto;
import com.example.entity.Category;
import com.example.entity.Comment;

public class CategoryMapper {
  public static CategoryDto toDto(Category category) {
    CategoryDto categoryDto = new CategoryDto();
    categoryDto.setName(category.getName());
    categoryDto.setType(category.getType());
    return categoryDto;
  }

  public static Category toEntity(CategoryDto categoryDto) {
    Category category = new Category();
    category.setName(categoryDto.getName());
    category.setType(categoryDto.getType());
    return category;
  }
}

package com.example.mapper;

import com.example.dto.CategoryDto;
import com.example.entity.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {
  public static CategoryDto toDto(Category category,CategoryDto categoryDto) {
    categoryDto.setName(category.getName());
    categoryDto.setType(category.getType());
    return categoryDto;
  }

  public static Category toCreateEntity(CategoryDto categoryDto) {
    Category category = new Category();
    category.setActive(categoryDto.isActive());
    category.setName(categoryDto.getName());
    category.setType(categoryDto.getType());
    return category;
  }
  public static Category toUpdateEntity(Category category,CategoryDto categoryDto) {
    category.setActive(categoryDto.isActive());
    category.setName(categoryDto.getName());
    category.setType(categoryDto.getType());
    return category;
  }

  public static List<CategoryDto> toListDto(List<Category> categoryList) {
    List<CategoryDto> categoryDtoList = new ArrayList<>();
    for(Category category:categoryList){
      CategoryDto categoryDto =new CategoryDto();
      categoryDtoList.add(CategoryMapper.toDto(category,categoryDto));
    }
    return categoryDtoList;
  }
}

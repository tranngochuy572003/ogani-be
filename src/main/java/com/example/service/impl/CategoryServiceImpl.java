package com.example.service.impl;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.exception.BadRequestException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import com.example.util.AppUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.*;

@Service
@Data
@AllArgsConstructor
public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryRepository categoryRepository;

  @Override
  public List<CategoryDto> getAllCategories() {
    return CategoryMapper.toListDto(categoryRepository.findAll());
  }

  @Override
  public List<CategoryDto> findByType(String type) {
    if (AppUtil.containsSpecialCharacters(type)) {
      throw new BadRequestException(FIELD_INVALID);
    }

    return CategoryMapper.toListDto(categoryRepository.findByType(type));

  }
  @Override
  public List<CategoryDto> findByName(String name) {
    if (AppUtil.containsSpecialCharacters(name)) {
      throw new BadRequestException(FIELD_INVALID);
    }
    return CategoryMapper.toListDto(categoryRepository.findByName(name));
  }

  @Override
  public List<CategoryDto> findByCreatedDate(LocalDate localDate) {
    return CategoryMapper.toListDto(categoryRepository.findByCreatedDateBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay()));
  }

  @Override
  public CategoryDto findById(String id) {
    if (AppUtil.containsSpecialCharacters(id)) {
      throw new BadRequestException(FIELD_INVALID);
    }
    Optional<Category> categoryOptional = categoryRepository.findById(id);
    if (categoryOptional.isPresent()) {
      CategoryDto categoryDto= new CategoryDto();
      return CategoryMapper.toDto(categoryOptional.get(),categoryDto);
    } else {
      throw new BadRequestException(FIELD_INVALID);
    }
  }

  @Override
  public void addCategory(CategoryDto categoryDto) {
    if (AppUtil.containsSpecialCharacters(categoryDto.getType())) {
      throw new BadRequestException("Type is invalid");
    }
    if (AppUtil.containsSpecialCharacters(categoryDto.getName())) {
      throw new BadRequestException("Name is invalid");
    }
    Category category = new Category();
    CategoryMapper.toEntity(category, categoryDto);
    categoryRepository.save(category);
  }

  @Override
  public void updateCategory(String id, CategoryDto categoryDto) {
    Optional<Category> optionalCategory = categoryRepository.findById(id);
    if (optionalCategory.isPresent()) {
      Category category = optionalCategory.get();
      if (AppUtil.containsSpecialCharacters(categoryDto.getType())) {
        throw new BadRequestException(FIELD_INVALID);
      }
      if (AppUtil.containsSpecialCharacters(categoryDto.getName())) {
        throw new BadRequestException(FIELD_INVALID);
      }
      List<Category> categoryList = categoryRepository.findByName(categoryDto.getName());
      if (categoryList.size() != 0) {
        throw new BadRequestException(VALUE_EXISTED);
      }
      Category categorySaved = CategoryMapper.toEntity(category, categoryDto);
      categoryRepository.save(categorySaved);
    } else {
      throw new BadRequestException(FIELD_INVALID);
    }
  }

  @Override
  public List<CategoryDto> getCategoriesActive() {
    return CategoryMapper.toListDto(categoryRepository.findCategoriesByActive());
  }

  @Override
  public void deleteCategory(String id) {
    Optional<Category> optionalCategory = categoryRepository.findById(id);
    if (optionalCategory.isPresent()) {
      categoryRepository.deleteById(id);
    } else {
      throw new BadRequestException(ITEM_NO_EXIST);
    }
  }
}

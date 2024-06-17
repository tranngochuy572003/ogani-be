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

import static com.example.common.MessageConstant.FIELD_INVALID;
import static com.example.common.MessageConstant.VALUE_EXISTED;


@Service
@Data
@AllArgsConstructor

public class CategoryServiceImpl implements CategoryService {
  @Autowired
  private CategoryRepository categoryRepository ;

  @Override
  public List<Category> getAllCategories() {
    return categoryRepository.findAll();
  }

  @Override
  public List<Category> findByType(String type) {
    if(AppUtil.containsSpecialCharacters(type)){
      throw new BadRequestException(FIELD_INVALID);
    }
    return categoryRepository.findByType(type);
  }

  @Override
  public List<Category> findByName(String name) {
    if(AppUtil.containsSpecialCharacters(name)){
      throw new BadRequestException(FIELD_INVALID);
    }
    return categoryRepository.findByName(name);
  }

  @Override
  public List<Category> findByCreatedDate(LocalDate localDate) {
    return categoryRepository.findByCreatedDateBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay());
  }

  @Override
  public Category findById(String id) {
    if(AppUtil.containsSpecialCharacters(id)){
      throw new BadRequestException(FIELD_INVALID);
    }
    Optional<Category> categoryOptional = categoryRepository.findById(id);
    if(categoryOptional.isPresent()){
      return categoryOptional.get();
    }else {
      throw  new BadRequestException(FIELD_INVALID);
    }
  }

  @Override
  public void addCategory(CategoryDto categoryDto) {
    if(AppUtil.containsSpecialCharacters(categoryDto.getType())){
      throw new BadRequestException("Type is invalid");
    }
    if(AppUtil.containsSpecialCharacters(categoryDto.getName())){
      throw new BadRequestException("Name is invalid");
    }
    Category category= new Category();
    CategoryMapper.toEntity(category,categoryDto);
    categoryRepository.save(category);
  }

  @Override
  public void updateCategory(String id, CategoryDto categoryDto) {
    Optional<Category> optionalCategory = categoryRepository.findById(id);
    if (optionalCategory.isPresent()) {
      Category category = optionalCategory.get();
      if(AppUtil.containsSpecialCharacters(categoryDto.getType())){
        throw new BadRequestException(FIELD_INVALID);
      }
      if(AppUtil.containsSpecialCharacters(categoryDto.getName())){
        throw new BadRequestException(FIELD_INVALID);
      }
      List<Category> categoryList = categoryRepository.findByName(categoryDto.getName());
      if(categoryList.size()!=0){
        throw new BadRequestException(VALUE_EXISTED);
      }
      Category categorySaved = CategoryMapper.toEntity(category, categoryDto);
      categoryRepository.save(categorySaved);
    } else {
      throw new BadRequestException(FIELD_INVALID);
    }
  }

  @Override
  public List<Category> getCategoriesActive(boolean active) {
    return categoryRepository.findCategoriesByIsActive(active);
  }
}

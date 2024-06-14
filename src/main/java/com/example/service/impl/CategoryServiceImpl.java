package com.example.service.impl;

import com.example.util.AppUtil;
import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.exception.BadRequestException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.CategoryService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.common.MessageConstant.*;


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
  public List<Category> findByCreatedDate(String date) {
    Date startDate  = AppUtil.checkDateValid(date);
    Date endDate = new Date(startDate.getTime() + 24 * 60 * 60 * 1000 - 1);
    return categoryRepository.findByCreatedDateBetween(startDate, endDate);
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
    Category category = CategoryMapper.toEntity(categoryDto);
    categoryRepository.save(category);
  }


}

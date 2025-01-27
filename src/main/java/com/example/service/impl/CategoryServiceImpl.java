package com.example.service.impl;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
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
        List<Category> categoryList = categoryRepository.findByType(type);
        if (AppUtil.containsSpecialCharacters(type)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        if (categoryList.isEmpty()) {
            throw new BadRequestException(VALUE_NO_EXIST);
        }
        return CategoryMapper.toListDto(categoryList);
    }

    @Override
    public CategoryDto findByName(String name) {
        if (AppUtil.containsSpecialCharacters(name)
                || name.chars().anyMatch(Character::isWhitespace)) {
            throw new BadRequestException(FIELD_INVALID);
        }
        CategoryDto categoryDto = new CategoryDto();
        Category category = categoryRepository.findByName(name);
        if (category == null) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        return CategoryMapper.toDto(category, categoryDto);

    }

    @Override
    public Category findCategoryByName(String name) {
        return (categoryRepository.findByName(name));
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
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        CategoryDto categoryDto = new CategoryDto();
        return CategoryMapper.toDto(categoryOptional.get(), categoryDto);
    }

    @Override
    public void addCategory(CategoryDto categoryDto) {
        if (AppUtil.containsSpecialCharacters(categoryDto.getType())
                || AppUtil.containsSpecialCharacters(categoryDto.getName())) {
            throw new BadRequestException(FIELD_INVALID);
        }
        boolean existCategory = categoryRepository.findByName(categoryDto.getName()) != null;
        if (existCategory) {
            throw new BadRequestException(VALUE_EXISTED);
        }
        Category category = CategoryMapper.toCreateEntity(categoryDto);
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(String id, CategoryDto categoryDto) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new BadRequestException(FIELD_INVALID);
        }
        Category category = optionalCategory.get();
        if (AppUtil.containsSpecialCharacters(categoryDto.getType())
                || AppUtil.containsSpecialCharacters(categoryDto.getName())) {
            throw new BadRequestException(FIELD_INVALID);
        }
        boolean existCategory = categoryRepository.findByName(categoryDto.getName()) != null;
        if (!category.getName().equals(categoryDto.getName()) && existCategory) {
            throw new BadRequestException(VALUE_EXISTED);
        }
        Category categorySaved = CategoryMapper.toUpdateEntity(category, categoryDto);
        categoryRepository.save(categorySaved);
    }

    @Override
    public List<CategoryDto> getCategoriesActive() {
        return CategoryMapper.toListDto(categoryRepository.findCategoriesByActive());
    }

    @Override
    public void deleteCategory(String id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new NotFoundException(VALUE_NO_EXIST);
        }
        categoryRepository.deleteById(id);

    }
}

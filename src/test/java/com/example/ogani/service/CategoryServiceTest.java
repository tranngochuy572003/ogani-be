package com.example.ogani.service;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.exception.NotFoundException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.impl.CategoryServiceImpl;
import com.example.util.AppUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static com.example.common.MessageConstant.FIELD_INVALID;
import static com.example.common.MessageConstant.VALUE_EXISTED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product("name", true, 100L, "description", "information", 100L, null, null, null);
        category = new Category("name", "type", true, Arrays.asList(product));
        category.setId("id");
        categoryDto = new CategoryDto("name", "type", true);
    }

    @Test
    void testGetCategoriesByTypeValidThenSuccess() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
            Mockito.when(categoryRepository.findByType(anyString())).thenReturn(Arrays.asList(category));
            categoryService.findByType(anyString());
            Assertions.assertEquals(CategoryMapper.toListDto(Arrays.asList(category)), Arrays.asList(categoryDto));
        }

    }

    @Test
    void testGetCategoriesByTypeWhenInvalidCategoryThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
            Mockito.when(categoryRepository.findByType(anyString())).thenReturn(Collections.emptyList());
            Assertions.assertThrows(BadRequestException.class, () -> categoryService.findByType("type"));
        }

    }

    @Test
    void testGetCategoriesByTypeInValidThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> categoryService.findByType("invalid_Type"));
        Assertions.assertEquals(FIELD_INVALID, badRequestException.getMessage());
    }

    @Test
    void testGetCategoryByNameThenSuccess() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        when(categoryRepository.findByName("name")).thenReturn(category);
        categoryService.findByName("name");
        Assertions.assertEquals(categoryDto, CategoryMapper.toDto(category, categoryDto));
    }

    @Test
    void testGetCategoryByNameInvalidFormatThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        Assertions.assertThrows(BadRequestException.class, () -> categoryService.findByName("name//"));
    }

    @Test
    void testGetCategoryByNameContainSpaceThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        Assertions.assertThrows(BadRequestException.class, () -> categoryService.findByName("name "));
    }

    @Test
    void testGetCategoryByNameInvalidThenThrowNotFoundException() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        when(categoryRepository.findByName("name")).thenReturn(null);
        Assertions.assertThrows(NotFoundException.class, () -> categoryService.findByName("name"));
    }


    @Test
    void testGetAllCategoriesThenSuccess() {
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryDto));
        Assertions.assertEquals(CategoryMapper.toListDto(Arrays.asList(category)), Arrays.asList(categoryDto));
    }

    @Test
    void findByIdThenSuccess() {
        CategoryDto newCategoryDto = new CategoryDto();
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        when(categoryRepository.findById("id")).thenReturn(Optional.of(category));
        categoryService.findById("id");
        Assertions.assertEquals(CategoryMapper.toDto(Optional.of(category).get(), newCategoryDto), newCategoryDto);
    }

    @Test
    void findByIdInvalidFormatThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        Assertions.assertThrows(BadRequestException.class, () -> categoryService.findById("id//"));
    }

    @Test
    void findByIdInCorrectThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        when(categoryRepository.findById("id")).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> categoryService.findById("id"));
    }


    @Test
    void getCreatedDateThenSuccess() {
        LocalDate localDate = LocalDate.now();
        when(categoryRepository.findByCreatedDateBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay())).thenReturn(Arrays.asList(category));
        when(categoryService.findByCreatedDate(LocalDate.now())).thenReturn(Arrays.asList(categoryDto));
        Assertions.assertEquals(CategoryMapper.toListDto(Arrays.asList(category)), Arrays.asList(categoryDto));
    }

    @Test
    void testAddCategoryThenSuccess() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        when(categoryRepository.findByName("name")).thenReturn(null);
        when(categoryRepository.save(CategoryMapper.toCreateEntity(categoryDto))).thenReturn(category);
        categoryService.addCategory(categoryDto);

        verify(categoryRepository).findByName("name");
        verify(categoryRepository).save(CategoryMapper.toCreateEntity(categoryDto));

    }

    @Test
    void testAddCategoryInValidNameThenThrowBadRequest() {
        categoryDto.setName("Invalid_Name");
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> categoryService.addCategory(categoryDto));
        Assertions.assertEquals(FIELD_INVALID, badRequestException.getMessage());
        verify(categoryRepository, never()).findByName(anyString());
    }

    @Test
    void testAddCategoryNameExistThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        when(categoryRepository.findByName("name")).thenReturn(category);

        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> categoryService.addCategory(categoryDto));
        Assertions.assertEquals(VALUE_EXISTED, badRequestException.getMessage());
        verify(categoryRepository, never()).save(CategoryMapper.toCreateEntity(categoryDto));

    }

    @Test
    void testUpdateCategoryThenSuccess() {
        when(categoryRepository.findById("id")).thenReturn(Optional.of(category));
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        when(categoryRepository.findByName(categoryDto.getName())).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);
        categoryService.updateCategory("id", categoryDto);
        Assertions.assertEquals(category, CategoryMapper.toUpdateEntity(category, categoryDto));

        verify(categoryRepository).findById(any(String.class));
        verify(categoryRepository).findByName(any(String.class));
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void testUpdateCategoryWhenNameOrTypeInvalidThenThrowBadRequest() {
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category));
        categoryDto.setName("invalidName//");
        categoryDto.setType("invalidType//");
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        Assertions.assertThrows(BadRequestException.class, () ->
                categoryService.updateCategory("id", categoryDto));
    }

    @Test
    void testUpdateCategoryWhenNameExistThenThrowBadRequest() {
        when(categoryRepository.findById(anyString())).thenReturn(Optional.of(category));
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        when(categoryRepository.findByName(anyString())).thenReturn(category);
        categoryDto.setName("otherName");
        Assertions.assertThrows(BadRequestException.class, () ->
                categoryService.updateCategory("id", categoryDto));
    }


    @Test
    void testUpdateCategoryWhenIdInCorrectThenThrowBadRequest() {
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(BadRequestException.class, () ->
                categoryService.updateCategory("id", categoryDto));
    }


    @Test
    void getCategoriesActiveThenSuccess() {
        when(categoryRepository.findCategoriesByActive()).thenReturn(Arrays.asList(category));
        when(categoryService.getCategoriesActive()).thenReturn(Arrays.asList(categoryDto));

        Assertions.assertEquals(Arrays.asList(categoryDto), CategoryMapper.toListDto(Arrays.asList(category)));
    }

    @Test
    void deleteCategoryThenSuccess() {
        when(categoryRepository.findById("id")).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById("id");
        categoryService.deleteCategory("id");

        verify(categoryRepository).findById(any(String.class));
        verify(categoryRepository).deleteById(any(String.class));
    }

    @Test
    void deleteCategoryWhenIdIncorrectThenThrowBadRequest() {
        when(categoryRepository.findById(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> categoryService.deleteCategory("id"));

    }

    @Test
    void testFindCategoryByNameThenSuccess() {
        categoryService.findCategoryByName(anyString());
        verify(categoryRepository).findByName(anyString());

    }

}

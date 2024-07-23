package com.example.ogani.service;

import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.exception.BadRequestException;
import com.example.mapper.CategoryMapper;
import com.example.repository.CategoryRepository;
import com.example.service.impl.CategoryServiceImpl;
import com.example.util.AppUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static com.example.common.MessageConstant.FIELD_INVALID;
import static com.example.common.MessageConstant.VALUE_EXISTED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;
    @Mock
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;
    private CategoryDto categoryDto;
    private CategoryMapper categoryMapper;


    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        product = new Product("name", true, 100L, "description", "information", 100L, null, null, null, null, null);
        category = new Category("name", "type", true, Arrays.asList(product), null);
        category.setId("id");
        categoryDto = new CategoryDto("name", "type", true);
        objectMapper = new ObjectMapper();

    }

    @Test
    void testGetCategoriesByTypeValidThenSuccess() throws Exception {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
            Mockito.when(categoryRepository.findByType("type")).thenReturn(Arrays.asList(category));
            Mockito.when(categoryService.findByType("type")).thenReturn(Arrays.asList(categoryDto));
            Assertions.assertEquals(CategoryMapper.toListDto(Arrays.asList(category)), Arrays.asList(categoryDto));
        }

    }

    @Test
    void testGetCategoriesByTypeInValidThenThrowBadRequest() throws Exception {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> categoryService.findByType("invalid_Type"));
        Assertions.assertEquals(FIELD_INVALID, badRequestException.getMessage());
    }

    @Test
    void testGetAllCategoriesThenSuccess() {
        Mockito.when(categoryRepository.findAll()).thenReturn(Arrays.asList(category));
        Mockito.when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryDto));

        Assertions.assertEquals(CategoryMapper.toListDto(Arrays.asList(category)), Arrays.asList(categoryDto));
    }

    @Test
    void findByIdThenSuccess() {
        CategoryDto newCategoryDto = new CategoryDto();
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        Mockito.when(categoryRepository.findById("id")).thenReturn(Optional.of(category));
        categoryService.findById("id");
        Assertions.assertEquals(CategoryMapper.toDto(Optional.of(category).get(), newCategoryDto), newCategoryDto);
    }

    @Test
    void getCreatedDateThenSuccess() {
        LocalDate localDate = LocalDate.now();
        Mockito.when(categoryRepository.findByCreatedDateBetween(localDate.atStartOfDay(), localDate.plusDays(1).atStartOfDay())).thenReturn(Arrays.asList(category));
        Mockito.when(categoryService.findByCreatedDate(LocalDate.now())).thenReturn(Arrays.asList(categoryDto));
        Assertions.assertEquals(CategoryMapper.toListDto(Arrays.asList(category)), Arrays.asList(categoryDto));
    }

    @Test
    void testAddCategoryThenSuccess() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        Mockito.when(categoryRepository.findByName("name")).thenReturn(null);
        Mockito.when(categoryRepository.save(CategoryMapper.toCreateEntity(categoryDto))).thenReturn(category);
        categoryService.addCategory(categoryDto);

        verify(categoryRepository, times(1)).findByName("name");
        verify(categoryRepository, times(1)).save(CategoryMapper.toCreateEntity(categoryDto));

    }

    @Test
    void testAddCategoryInValidNameThenThrowBadRequest() {
        categoryDto.setName("Invalid_Name");
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(true);
        }
        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> categoryService.addCategory(categoryDto));
        Assertions.assertEquals("Name is invalid", badRequestException.getMessage());
        verify(categoryRepository, never()).findByName(anyString());
    }

    @Test
    void testAddCategoryNameExistThenThrowBadRequest() {
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        Mockito.when(categoryRepository.findByName("name")).thenReturn(category);

        BadRequestException badRequestException = Assertions.assertThrows(BadRequestException.class, () -> categoryService.addCategory(categoryDto));
        Assertions.assertEquals(VALUE_EXISTED, badRequestException.getMessage());
        verify(categoryRepository, never()).save(CategoryMapper.toCreateEntity(categoryDto));

    }

    @Test
    void testUpdateCategoryThenSuccess() {
        Mockito.when(categoryRepository.findById("id")).thenReturn(Optional.of(category));
        try (MockedStatic<AppUtil> appUtilMock = mockStatic(AppUtil.class)) {
            appUtilMock.when(() -> AppUtil.containsSpecialCharacters(anyString())).thenReturn(false);
        }
        Mockito.when(categoryRepository.findByName(categoryDto.getName())).thenReturn(null);
        Mockito.when(categoryRepository.save(category)).thenReturn(category);
        categoryService.updateCategory("id", categoryDto);
        Assertions.assertEquals(category, CategoryMapper.toUpdateEntity(category, categoryDto));

        verify(categoryRepository, times(1)).findById(any(String.class));
        verify(categoryRepository, times(1)).findByName(any(String.class));
        verify(categoryRepository, times(1)).save(any(Category.class));
    }


    @Test
    void getCategoriesActiveThenSuccess(){
        Mockito.when(categoryRepository.findCategoriesByActive()).thenReturn(Arrays.asList(category));
        Mockito.when(categoryService.getCategoriesActive()).thenReturn(Arrays.asList(categoryDto));

        Assertions.assertEquals(Arrays.asList(categoryDto), CategoryMapper.toListDto(Arrays.asList(category)));
    }

    @Test
    void deleteCategoryThenSuccess(){
        Mockito.when(categoryRepository.findById("id")).thenReturn(Optional.of(category));
        doNothing().when(categoryRepository).deleteById("id");
        categoryService.deleteCategory("id");

        verify(categoryRepository, times(1)).findById(any(String.class));
        verify(categoryRepository, times(1)).deleteById(any(String.class));
    }
}

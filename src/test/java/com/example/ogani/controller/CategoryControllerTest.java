package com.example.ogani.controller;

import com.example.controller.CategoryController;
import com.example.dto.CategoryDto;
import com.example.entity.Category;
import com.example.entity.Product;
import com.example.mapper.CategoryMapper;
import com.example.service.impl.CategoryServiceImpl;
import com.example.util.AppUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static com.example.common.MessageConstant.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest {
    @InjectMocks
    private CategoryController categoryController;
    @Mock
    private CategoryServiceImpl categoryService;
    @Mock
    private AppUtil appUtil;
    @Mock
    private CategoryMapper categoryMapper;


    private Product product;
    private Category category;
    private CategoryDto categoryDto;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
        objectMapper = new ObjectMapper();
        product = new Product("name", true, 100L, "description", "information", 100L, null, null, null);
        category = new Category("name", "type", true, Arrays.asList(product));
        category.setId("id");
        categoryDto = new CategoryDto("name", "type", true);
    }

    @Test
    void testGetCategoriesByTypeThenSuccess() throws Exception {
        Mockito.when(categoryService.findByType("type")).thenReturn(CategoryMapper.toListDto(Arrays.asList(category)));
        mockMvc.perform(get("/api/v1/categories/getCategoriesByType/{type}", "type")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data[0].name").value(category.getName()))
                .andExpect(jsonPath("$.data[0].type").value("type"));
    }


    @Test
    void testGetAllCategoriesThenSuccess() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(Arrays.asList(categoryDto));
        mockMvc.perform(get("/api/v1/categories/getAllCategories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Successfully"));
    }

    @Test
    void testFindCategoryByIdThenSuccess() throws Exception {
        Mockito.when(categoryService.findById("categoryId")).thenReturn(categoryDto);
        mockMvc.perform(get("/api/v1/categories/getById/{categoryId}", "categoryId")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data.name").value(category.getName()))
                .andExpect(jsonPath("$.data.type").value(category.getType()));

    }


    @Test
    void testFindCategoryByNameThenSuccess() throws Exception {
        Mockito.when(categoryService.findByName("name")).thenReturn(categoryDto);
        mockMvc.perform(get("/api/v1/categories/getByName/{name}", "name")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Successfully"))
                .andExpect(jsonPath("$.data.name").value(category.getName()))
                .andExpect(jsonPath("$.data.type").value(category.getType()));
    }


    @Test
    void testAddCategoryThenSuccess() throws Exception {
        categoryService.addCategory(categoryDto);
        mockMvc.perform(post("/api/v1/categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(ITEM_CREATED_SUCCESS));
    }
    @Test
    void testUpdateCategoryThenSuccess() throws Exception {
        CategoryDto newCategoryDto = new CategoryDto("newName", "newType", true);
        mockMvc.perform(patch("/api/v1/categories/update/{id}", "id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCategoryDto)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value(ITEM_UPDATED_SUCCESS));
    }


    @Test
    void testGetCategoriesActiveThenSuccess() throws Exception {
        Mockito.when(categoryService.getCategoriesActive()).thenReturn(Arrays.asList(categoryDto));
        mockMvc.perform(get("/api/v1/categories/getCategoriesActive")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.message").value("Successfully"));

    }

    @Test
    void testDeleteCategoryThenSuccess() throws Exception {
        categoryService.deleteCategory("id");
        mockMvc.perform(delete("/api/v1/categories/deleteCategory/{id}", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.data").value("No contents"))
                .andExpect(jsonPath("$.message").value(ITEM_DELETED_SUCCESS));
    }
}

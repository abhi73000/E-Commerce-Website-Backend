package com.abhi.ecom.services.admin.category;

import com.abhi.ecom.dto.CategoryDto;
import com.abhi.ecom.model.Category;
import com.abhi.ecom.repository.CategoryRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CategoryServiceImplTest {
    AutoCloseable autoCloseable;
    @Mock
    CategoryRepository categoryRepository;

    private CategoryService categoryService;

    Category category;
    CategoryDto categoryDto;
    List<Category> categories;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        categoryService = new CategoryServiceImpl(categoryRepository);

        category = Category.builder()
                .id(1L)
                .name("demoCategory")
                .description("demoDescription")
                .build();

        categoryDto = category.getDto();
        categories = new ArrayList<>();
        categories.add(category);

    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void createCategory() {
        when(categoryRepository.save(any())).thenReturn(category);
        assertEquals(category.getId(),categoryService.createCategory(categoryDto).getId());
    }

    @Test
    void getAllCategory() {
        when(categoryRepository.findAll()).thenReturn(categories);
        assertEquals(categories.get(0).getDescription(),categoryService.getAllCategory().get(0).getDescription());
    }
}
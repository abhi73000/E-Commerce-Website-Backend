package com.abhi.ecom.services.admin.category;

import java.util.List;

import com.abhi.ecom.dto.CategoryDto;
import com.abhi.ecom.model.Category;

public interface CategoryService {
	 Category createCategory(CategoryDto categoryDto);
	 List<Category> getAllCategory();
}

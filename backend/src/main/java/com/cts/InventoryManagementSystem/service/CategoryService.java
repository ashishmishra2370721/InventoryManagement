package com.cts.InventoryManagementSystem.service;

import com.cts.InventoryManagementSystem.dto.CategoryDTO;
import com.cts.InventoryManagementSystem.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDTO categoryDTO);
    Response getAllCategories();
    Response getCategoryById(Long id);
    Response updateCategory(Long id, CategoryDTO categoryDTO);
    Response deleteCategory(Long id);
}

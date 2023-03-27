package com.itgate.ecommerce.service;



import com.itgate.ecommerce.models.Category;

import java.util.List;

public interface CategoryService {
    public Category findCategoryById(Long id);
    public List<Category> findCategories();
    public void deleteCategory(Long id);
    public Category createCategory(Category category);
    public Category updateCategory(Category category);
}

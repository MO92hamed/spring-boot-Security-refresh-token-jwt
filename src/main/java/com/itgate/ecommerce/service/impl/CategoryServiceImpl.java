package com.itgate.ecommerce.service.impl;


import com.itgate.ecommerce.models.Category;
import com.itgate.ecommerce.repository.CategoryRepository;
import com.itgate.ecommerce.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Override
    public Category findCategoryById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException(String.format("Category with id [%s] is not found in our database", id)));
    }

    @Override
    public List<Category> findCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        return categoryRepository.save(category);
    }
}

package com.itgate.ecommerce.controllers;


import com.itgate.ecommerce.models.Category;
import com.itgate.ecommerce.service.impl.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {
    private final CategoryServiceImpl categoryService;

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id){
        return categoryService.findCategoryById(id);
    }

    @GetMapping
    public List<Category> getAllCategories(){
        return categoryService.findCategories();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteCategory(@PathVariable Long id){
        Map<String, Boolean> response = new HashMap<>();
        categoryService.deleteCategory(id);
        try{
            response.put("Deleted", Boolean.TRUE);
            return response;
        }catch(Exception e){
            response.put("failed to delete", Boolean.FALSE);
            return response;
        }
    }

    @PostMapping
    public Category addCategory(@RequestBody Category category){
        return categoryService.createCategory(category);
    }

    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable Long id, @RequestBody Category category){
        Category c1 = categoryService.findCategoryById(id);
        if (c1 != null)
            return categoryService.updateCategory(category);
        else
            throw new RuntimeException("Failed to update category");
    }
}

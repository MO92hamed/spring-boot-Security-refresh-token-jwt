package com.itgate.ecommerce.controllers;


import com.itgate.ecommerce.models.Category;
import com.itgate.ecommerce.models.Subcategory;
import com.itgate.ecommerce.repository.CategoryRepository;
import com.itgate.ecommerce.service.impl.CategoryServiceImpl;
import com.itgate.ecommerce.service.impl.SubcategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subcategory")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SubcategoryController {

    private final SubcategoryServiceImpl subcategoryService;
    private final CategoryRepository categoryRepository;
    private final CategoryServiceImpl categoryServiceImpl;

    @GetMapping("/{id}")
    public Subcategory getSubcategoryById(@PathVariable Long id){
        return subcategoryService.findSubById(id);
    }

    @GetMapping
    public List<Subcategory> getAllSubs(){
        return subcategoryService.findAllSubs();
    }

    @DeleteMapping("/{id}")
    public Map<String, Boolean> deleteSubById(@PathVariable Long id){
        Map<String, Boolean> response = new HashMap<>();
        subcategoryService.deleteSub(id);
        try{
            response.put("Subcategory deleted", Boolean.TRUE);
            return response;
        }catch(Exception e){
            response.put("Failed to delete subcategory", Boolean.FALSE);
            return response;
        }
    }

    @PostMapping("/{idcategory}")
    public Subcategory addSubcategory(@RequestBody Subcategory subcategory, @PathVariable Long idcategory){
        Category category = categoryServiceImpl.findCategoryById(idcategory);
        subcategory.setCategory(category);
        return subcategoryService.createSubcategory(subcategory);
    }

    @PutMapping("/{id}")
    public Subcategory updateSubcategory(@PathVariable Long id, @RequestBody Subcategory subcategory) {
        Subcategory s1 = subcategoryService.findSubById(id);

        if (s1 != null) {
            subcategory.setCategory(s1.getCategory());
            return subcategoryService.updateSubcategory(subcategory);

        } else {
            throw new RuntimeException("Failed to update subcategory");

        }
    }
}

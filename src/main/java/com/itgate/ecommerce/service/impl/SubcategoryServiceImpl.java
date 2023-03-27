package com.itgate.ecommerce.service.impl;


import com.itgate.ecommerce.models.Subcategory;
import com.itgate.ecommerce.repository.SubcategoryRepository;
import com.itgate.ecommerce.service.SubcategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubcategoryServiceImpl implements SubcategoryService {

    private final SubcategoryRepository subcategoryRepository;
    @Override
    public Subcategory findSubById(Long id) {
        return subcategoryRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("Subcategory with id [%s] not found in our database", id)));
    }

    @Override
    public List<Subcategory> findAllSubs() {
        return subcategoryRepository.findAll();
    }

    @Override
    public void deleteSub(Long id) {
        subcategoryRepository.deleteById(id);
    }

    @Override
    public Subcategory createSubcategory(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }

    @Override
    public Subcategory updateSubcategory(Subcategory subcategory) {
        return subcategoryRepository.save(subcategory);
    }
}

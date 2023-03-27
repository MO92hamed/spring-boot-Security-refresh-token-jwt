package com.itgate.ecommerce.service;




import com.itgate.ecommerce.models.Subcategory;

import java.util.List;

public interface SubcategoryService {

    public Subcategory findSubById(Long id);
    public List<Subcategory> findAllSubs();
    public void deleteSub(Long id);
    public Subcategory createSubcategory(Subcategory subcategory);
    public Subcategory updateSubcategory(Subcategory subcategory);
}

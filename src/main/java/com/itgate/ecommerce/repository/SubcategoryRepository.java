package com.itgate.ecommerce.repository;


import com.itgate.ecommerce.models.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
}

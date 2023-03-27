package com.itgate.ecommerce.service;



import com.itgate.ecommerce.models.Product;

import java.util.List;

public interface ProductService {
    public Product findById(Long id);
    public List<Product> findAllProducts();
    public void  deleteByID(Long id);
    public Product addProduct(Product product);
    public Product updateProduct(Product product);
}

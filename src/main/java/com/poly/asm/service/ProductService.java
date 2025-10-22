package com.poly.asm.service;

import com.poly.asm.entity.Product;
import java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product findById(Integer id);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByBrandId(Integer brandId);
    List<Product> findByBrandAndCategory(Integer brandId, Integer categoryId);
    Product save(Product product);
    void delete(Integer id);

    List<Product> searchByName(String keyword);
}

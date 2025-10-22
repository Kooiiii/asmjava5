package com.poly.asm.service;

import com.poly.asm.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByBrandId(Integer brandId, Pageable pageable);
    Page<Product> findByCategoryId(Integer categoryId, Pageable pageable);
    Page<Product> findByBrandAndCategory(Integer brandId, Integer categoryId, Pageable pageable);
    Page<Product> searchByName(String keyword, Pageable pageable);
}
